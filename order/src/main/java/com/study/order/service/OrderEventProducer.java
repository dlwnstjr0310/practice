package com.study.order.service;

import com.study.order.domain.entity.OrderDetail;
import com.study.order.domain.entity.order.Order;
import com.study.order.domain.event.consumer.PaymentResultEvent;
import com.study.order.domain.event.producer.AddressEvent;
import com.study.order.domain.event.producer.InventoryManagementEvent;
import com.study.order.domain.event.producer.OrderCreatedEvent;
import com.study.order.exception.order.BeforePurchaseTimeException;
import com.study.order.exception.order.OrderBeenCanceledException;
import com.study.order.exception.order.OutOfStockException;
import com.study.order.model.request.DiscountProductOrderRequestDTO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.study.order.domain.entity.order.Status.ORDER_PROGRESS;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

	private static final String REDISSON_KEY_PREFIX = "lock_";
	private static final String PRODUCT_KEY_PREFIX = "product:";
	private static final String ORDER_CREATED_EVENT = "order-created-event";
	private static final String ADDRESS_UPDATE_EVENT = "address-update-event";
	private static final String ADDRESS_STORE_EVENT = "address-store-event";
	private static final String INVENTORY_MANAGEMENT_EVENT = "inventory-management-event";

	private final Random random = new Random();

	private final OrderService orderService;
	private final RedisService redisService;
	private final RedissonClient redissonClient;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void createOrder(DiscountProductOrderRequestDTO request) {

		if (random.nextDouble() < 0.2) {
//			return;
			throw new OrderBeenCanceledException();
		}

		String key = PRODUCT_KEY_PREFIX + request.product().productId();

		RLock lock = redissonClient.getLock(REDISSON_KEY_PREFIX + key);

		lock.lock();

		int currentQuantity, price;
		try {

			String value = redisService.getValue(key);

			currentQuantity = Integer.parseInt(value.split("\\|")[0]);
			price = Integer.parseInt(value.split("\\|")[1]);
			ZonedDateTime saleDateTime = ZonedDateTime.parse(value.split("\\|")[2]);

			ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

			if (now.isBefore(saleDateTime)) {
				throw new BeforePurchaseTimeException();
			}

			if (currentQuantity <= 0) {
				throw new OutOfStockException();
			}

			currentQuantity = currentQuantity - request.product().quantity();

			redisService.storeInRedis(key, currentQuantity + "|" + price + "|" + saleDateTime);

		} finally {
			lock.unlock();
		}

		Order order = Order.builder()
				.memberId(request.memberId())
				.totalPrice(price * request.product().quantity())
				.destinationAddress(request.destinationAddress())
				.build();

		OrderDetail orderDetail = OrderDetail.builder()
				.order(order)
				.productId(request.product().productId())
				.quantity(request.product().quantity())
				.price(price)
				.build();

		orderService.store(orderDetail, ORDER_PROGRESS);

		sendEvent(ORDER_CREATED_EVENT,
				new OrderCreatedEvent(
						order.getId(),
						request.product().productId(),
						request.product().quantity(),
						ORDER_PROGRESS
				)
		);

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

		Map<Long, Integer> productQuantityMap = new HashMap<>();

		productQuantityMap.put(event.productId(), event.quantity());

		sendEvent(INVENTORY_MANAGEMENT_EVENT,
				new InventoryManagementEvent(
						productQuantityMap,
						false
				)
		);
	}

	public void modifyOrderStatus(Long orderId, String status) {

		handleInventoryManagementEvent(orderService.modifyOrderStatus(orderId, status));
	}

	private void handleInventoryManagementEvent(List<OrderDetail> orderDetailList) {

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
