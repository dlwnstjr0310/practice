package com.study.order.service;

import com.study.order.domain.entity.OrderDetail;
import com.study.order.domain.entity.order.Order;
import com.study.order.domain.event.consumer.PaymentResultEvent;
import com.study.order.domain.event.producer.AddressEvent;
import com.study.order.domain.event.producer.InventoryManagementEvent;
import com.study.order.domain.event.producer.OrderCreatedEvent;
import com.study.order.exception.order.OutOfStockException;
import com.study.order.model.request.DiscountProductOrderRequestDTO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.study.order.domain.entity.order.Status.ORDER_COMPLETED;
import static com.study.order.domain.entity.order.Status.PAYMENT_COMPLETED;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

	private static final String REDISSON_KEY_PREFIX = "lock_";
	private static final String PRODUCT_KEY_PREFIX = "product:";
	private static final String ORDER_CREATED_EVENT = "order-created-event";
	private static final String ADDRESS_UPDATE_EVENT = "address-update-event";
	private static final String ADDRESS_STORE_EVENT = "address-store-event";
	private static final String INVENTORY_MANAGEMENT_EVENT = "inventory-management-event";

	private final OrderService orderService;
	private final RedisService redisService;
	private final RedissonClient redissonClient;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	private final ConcurrentHashMap<String, OrderDetail> map = new ConcurrentHashMap<>();

	//todo: 동시 요청수가 커넥션풀보다 많아지면 개느려짐; 해결하셈
	public void createOrder(DiscountProductOrderRequestDTO request) {

		String key = PRODUCT_KEY_PREFIX + request.product().productId().toString();
		RLock lock = redissonClient.getLock(REDISSON_KEY_PREFIX + key);

		lock.lock();
		Order order;
		OrderDetail orderDetail;

		String tempId = UUID.randomUUID().toString();

		try {

			String value = redisService.getValue(key);

			int currentQuantity = Integer.parseInt(value.split(":")[0]);
			int price = Integer.parseInt(value.split(":")[1]);

			if (currentQuantity <= 0) {
				throw new OutOfStockException();
			}

			currentQuantity = currentQuantity - request.product().quantity();

			redisService.storeInRedis(key, currentQuantity + ":" + price);

			order = Order.builder()
					.memberId(request.memberId())
					.totalPrice(price * request.product().quantity())
					.destinationAddress(request.destinationAddress())
					.build();

			orderDetail = OrderDetail.builder()
					.order(order)
					.productId(request.product().productId())
					.quantity(request.product().quantity())
					.price(price)
					.build();

			sendEvent(ORDER_CREATED_EVENT,
					new OrderCreatedEvent(
							tempId,
							orderDetail.getProductId(),
							orderDetail.getQuantity(),
							ORDER_COMPLETED
					)
			);

			map.put(tempId, orderDetail);

		} finally {
			lock.unlock();
		}

		if (request.isStoreInAddress()) {
			AddressEvent event = new AddressEvent(
					request.memberId(),
					request.addressAlias(),
					request.destinationAddress(),
					request.zipCode(),
					request.phone()
			);

			if (request.isDefault()) {
				sendEvent(ADDRESS_UPDATE_EVENT, event);
			} else {
				sendEvent(ADDRESS_STORE_EVENT, event);
			}
		}
	}

	public void handleInventoryManagementEvent(PaymentResultEvent event) {

		if (map.containsKey(event.orderId())) {

			if (event.status().equals(PAYMENT_COMPLETED)) {
				Map<Long, Integer> productQuantityMap = new HashMap<>();

				productQuantityMap.put(event.productId(), event.quantity());

				sendEvent(INVENTORY_MANAGEMENT_EVENT,
						new InventoryManagementEvent(
								productQuantityMap,
								false
						)
				);
			}

			orderService.store(map.remove(event.orderId()), event.status());
		}
	}

	public void handleInventoryManagementEvent(List<OrderDetail> orderDetailList) {

		Map<Long, Integer> productQuantityMap = new HashMap<>();

		orderDetailList.forEach(orderDetail ->
				productQuantityMap.put(orderDetail.getProductId(), orderDetail.getQuantity())
		);

		sendEvent(INVENTORY_MANAGEMENT_EVENT,
				new InventoryManagementEvent(
						productQuantityMap,
						true
				)
		);
	}

	private void sendEvent(String topic, Object event) {
		kafkaTemplate.send(topic, event);
	}

}
