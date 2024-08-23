package com.study.product.domain.event.consumer;

public record OrderSuccessEvent(

		Long productId,

		Integer quantity

) {
}
