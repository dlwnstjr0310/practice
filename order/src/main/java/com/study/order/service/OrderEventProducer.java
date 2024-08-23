package com.study.order.service;

import com.study.order.domain.entity.OrderDetail;
import com.study.order.domain.entity.order.Order;
import com.study.order.domain.event.consumer.PaymentResultEvent;
import com.study.order.domain.event.producer.AddressEvent;
import com.study.order.domain.event.producer.OrderCreatedEvent;
import com.study.order.domain.event.producer.OrderSuccessEvent;
import com.study.order.exception.order.OutOfStockException;
import com.study.order.model.request.DiscountProductOrderRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.study.order.domain.entity.order.Status.ORDER_COMPLETED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventProducer {

	private static final String REDISSON_KEY_PREFIX = "lock_";
	private static final String PRODUCT_KEY_PREFIX = "product:";
	private static final String ORDER_CREATED_EVENT = "order-created-event";
	private static final String ADDRESS_UPDATE_EVENT = "address-update-event";
	private static final String ADDRESS_STORE_EVENT = "address-store-event";
	private static final String ORDER_SUCCESS_EVENT = "order-success-event";

	private final OrderService orderService;
	private final RedisService redisService;
	private final RedissonClient redissonClient;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	private final ConcurrentHashMap<String, OrderDetail> map = new ConcurrentHashMap<>();

	//todo: 동시 요청수가 커넥션풀보다 많아지면 개느려짐; 해결하셈
	@Transactional
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

			log.info("주문 이벤트 생성");
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

	public void createOrderSuccessEvent(PaymentResultEvent event) {

		if (map.containsKey(event.orderId())) {

			log.info("주문 성공");
			sendEvent(ORDER_SUCCESS_EVENT,
					new OrderSuccessEvent(
							event.productId(),
							event.quantity()
					)
			);

			orderService.store(map.remove(event.orderId()));
		}
	}

	private void sendEvent(String topic, Object event) {
		kafkaTemplate.send(topic, event);
	}

}
