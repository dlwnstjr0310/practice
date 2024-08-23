package com.study.product.service;

import com.study.product.domain.event.consumer.OrderSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventListener {

	private static final String ORDER_SUCCESS_EVENT = "order-success-event";

	private final ProductAsyncService productService;

	@KafkaListener(topics = ORDER_SUCCESS_EVENT,
			properties = "spring.json.value.default.type=com.study.product.domain.event.consumer.OrderSuccessEvent"
	)
	public void handleOrderSuccessEvent(OrderSuccessEvent event) {
		productService.decreaseStock(event);
	}

}
