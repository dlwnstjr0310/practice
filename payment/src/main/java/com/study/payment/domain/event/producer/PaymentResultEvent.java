package com.study.payment.domain.event.producer;

import com.study.payment.domain.common.Status;

public record PaymentResultEvent(

		String orderId,

		Long productId,

		Integer quantity,

		Status status

) {
}
