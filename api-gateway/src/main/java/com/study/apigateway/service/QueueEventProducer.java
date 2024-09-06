package com.study.apigateway.service;

import com.study.apigateway.auth.model.request.DiscountProductOrderRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueEventProducer {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void sendQueueEvent(DiscountProductOrderRequestDTO orderRequestDTO) {
		kafkaTemplate.send("queue-event", orderRequestDTO);
	}
}
