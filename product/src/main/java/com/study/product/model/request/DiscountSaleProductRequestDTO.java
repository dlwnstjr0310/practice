package com.study.product.model.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DiscountSaleProductRequestDTO(

		@NotNull
		Long id,

		@NotNull
		Integer discountRate,

		@NotNull
		Integer quantity,

		@NotNull
		LocalDateTime saleDateTime

) {
}
