package com.study.order.domain.event.consumer;

import com.study.order.domain.entity.order.Status;

public record PaymentResultEvent(

		Long orderId,

		Long productId,

		Integer quantity,

		Status status

) {
}
