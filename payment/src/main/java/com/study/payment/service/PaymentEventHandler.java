package com.study.payment.service;

import com.study.payment.domain.event.consumer.OrderCreatedEvent;
import com.study.payment.domain.event.producer.PaymentResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.study.payment.domain.common.Status.*;

@Service
@RequiredArgsConstructor
public class PaymentEventHandler {

	private static final String ORDER_CREATED_EVENT = "order-created-event";
	private static final String PAYMENT_RESULT_EVENT = "payment-result-event";

	private final Random random = new Random();

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@KafkaListener(topics = ORDER_CREATED_EVENT)
	public void handleOrderCreatedEvent(OrderCreatedEvent event) {

		if (random.nextDouble() < 0.2) {
			// 고객 변심 이탈
			sendEvent(PAYMENT_RESULT_EVENT,
					new PaymentResultEvent(
							event.orderId(),
							event.productId(),
							event.quantity(),
							ORDER_CANCELED
					));
			return;
		}

		if (random.nextDouble() < 0.2) {
			// 결제 실패 이탈
			sendEvent(PAYMENT_RESULT_EVENT,
					new PaymentResultEvent(
							event.orderId(),
							event.productId(),
							event.quantity(),
							PAYMENT_FAILED
					));
			return;
		}

		// 결제 완료
		sendEvent(PAYMENT_RESULT_EVENT,
				new PaymentResultEvent(
						event.orderId(),
						event.productId(),
						event.quantity(),
						PAYMENT_COMPLETED
				)
		);

	}

	public void sendEvent(String topic, Object event) {
		kafkaTemplate.send(topic, event);
	}

}
