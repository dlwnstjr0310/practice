package com.study.order.domain.event.producer;

public record OrderSuccessEvent(

		Long productId,

		Integer quantity

) {
}
