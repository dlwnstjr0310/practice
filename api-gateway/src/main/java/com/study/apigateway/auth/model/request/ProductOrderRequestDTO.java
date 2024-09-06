package com.study.apigateway.auth.model.request;

import jakarta.validation.constraints.NotNull;

public record ProductOrderRequestDTO(

		@NotNull
		Long productId,

		@NotNull
		Integer quantity

) {
}
