package com.study.order.domain.event.producer;

import com.study.order.domain.entity.order.Status;

public record OrderCreatedEvent(

		Long orderId,

		Long productId,

		Integer quantity,

		Status status

) {
}
