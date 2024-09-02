package com.study.payment.domain.event.producer;

import com.study.payment.domain.common.Status;

public record PaymentResultEvent(

		Long orderId,

		Long productId,

		Integer quantity,

		Status status

) {
}
