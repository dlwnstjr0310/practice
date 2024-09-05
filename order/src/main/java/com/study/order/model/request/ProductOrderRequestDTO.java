package com.study.order.model.request;

import jakarta.validation.constraints.NotNull;

public record ProductOrderRequestDTO(

		@NotNull
		Long productId,

		@NotNull
		Integer quantity

) {
}
