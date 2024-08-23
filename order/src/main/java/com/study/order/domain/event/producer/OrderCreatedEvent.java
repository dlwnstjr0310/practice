package com.study.order.domain.event.producer;

import com.study.order.domain.entity.order.Status;

public record OrderCreatedEvent(

		String orderId,

		Long productId,

		Integer quantity,

		Status status

) {
}
