package com.study.order.service;

import com.study.order.domain.event.consumer.PaymentResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.study.order.domain.entity.order.Status.PAYMENT_COMPLETED;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventListener {

	private static final String PAYMENT_RESULT_EVENT = "payment-result-event";
	private static final String REDISSON_KEY_PREFIX = "lock_";
	private static final String PRODUCT_KEY_PREFIX = "product:";


	private final RedisService redisService;
	private final RedissonClient redissonClient;
	private final OrderEventProducer orderEventProducer;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@KafkaListener(topics = PAYMENT_RESULT_EVENT,
			properties = "spring.json.value.default.type=com.study.order.domain.event.consumer.PaymentResultEvent"
	)
	public void handlePaymentResultEvent(PaymentResultEvent event) {

		if (event.status().equals(PAYMENT_COMPLETED)) {
			orderEventProducer.createOrderSuccessEvent(event);
		} else {

			String key = PRODUCT_KEY_PREFIX + event.productId().toString();
			RLock lock = redissonClient.getLock(REDISSON_KEY_PREFIX + key);

			lock.lock();

			log.info("주문 실패 이벤트 생성");
			try {

				String value = redisService.getValue(key);

				int currentQuantity = Integer.parseInt(value.split(":")[0]) + event.quantity();
				int price = Integer.parseInt(value.split(":")[1]);

				redisService.storeInRedis(key, currentQuantity + ":" + price);

			} finally {
				lock.unlock();
			}
		}
	}
}
