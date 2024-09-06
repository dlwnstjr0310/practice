package com.study.order.service;

import com.study.order.domain.entity.OrderDetail;
import com.study.order.domain.entity.order.Order;
import com.study.order.domain.event.producer.AddressEvent;
import com.study.order.domain.event.producer.OrderCreatedEvent;
import com.study.order.exception.order.BeforePurchaseTimeException;
import com.study.order.exception.order.OutOfStockException;
import com.study.order.exception.server.TimeoutException;
import com.study.order.model.request.DiscountProductOrderRequestDTO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.study.order.domain.entity.order.Status.ORDER_PROGRESS;

@Service
@RequiredArgsConstructor
public class QueueEventListener {

	private static final String QUEUE_EVENT = "queue-event";
	private static final String REDISSON_KEY_PREFIX = "lock_";
	private static final String PRODUCT_KEY_PREFIX = "product:";
	private static final String ORDER_CREATED_EVENT = "order-created-event";
	private static final String ADDRESS_UPDATE_EVENT = "address-update-event";
	private static final String ADDRESS_STORE_EVENT = "address-store-event";

	private final Random random = new Random();

	private final OrderService orderService;
	private final RedisService redisService;
	private final RedissonClient redissonClient;
	private final OrderEventProducer orderEventProducer;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@KafkaListener(topics = QUEUE_EVENT,
			properties = "spring.json.value.default.type=com.study.order.model.request.DiscountProductOrderRequestDTO"
	)
	public void createOrder(DiscountProductOrderRequestDTO request) {

		if (random.nextDouble() < 0.2) {
			return;
		}

		String key = PRODUCT_KEY_PREFIX + request.product().productId();

		RLock lock = redissonClient.getLock(REDISSON_KEY_PREFIX + key);

		try {
			if (!lock.tryLock(5, 3, TimeUnit.SECONDS)) {
				throw new TimeoutException();
			}
		} catch (InterruptedException e) {
			throw new TimeoutException();
		}

		int currentQuantity, price;

		try {

			String value = redisService.getValue(key);

			String[] parts = value.split("\\|");

			currentQuantity = Integer.parseInt(parts[0]);
			price = Integer.parseInt(parts[1]);
			ZonedDateTime saleDateTime = ZonedDateTime.parse(parts[2]);

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

		orderEventProducer.sendEvent(ORDER_CREATED_EVENT,
				new OrderCreatedEvent(
						order.getId(),
						request.product().productId(),
						request.product().quantity(),
						ORDER_PROGRESS
				)
		);

		redisService.storeInRedis(
				request.memberId() + "|" + request.product().productId(),
				ORDER_PROGRESS.getDescription()
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
				orderEventProducer.sendEvent(ADDRESS_UPDATE_EVENT, event);
			} else {
				orderEventProducer.sendEvent(ADDRESS_STORE_EVENT, event);
			}
		}
	}
}
