package com.study.payment.domain.event.consumer;

import com.study.payment.domain.common.Status;

public record OrderCreatedEvent(

		Long orderId,

		Long productId,

		Integer quantity,

		Status status

) {
}
