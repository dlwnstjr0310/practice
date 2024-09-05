package com.study.order.model.response.order;

import jakarta.validation.constraints.NotNull;

public record ProductOrderResponseDTO(

		@NotNull
		Long productId,

		@NotNull
		Integer quantity,

		@NotNull
		Integer piecePrice
) {

	public static ProductOrderResponseDTO of(Long id, Integer quantity, Integer price) {

		return new ProductOrderResponseDTO(
				id,
				quantity,
				price
		);
	}
}
