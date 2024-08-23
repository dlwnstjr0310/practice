package com.study.product.model.request;

import jakarta.validation.constraints.NotNull;

public record DiscountSaleProductRequestDTO(

		@NotNull
		Long id,

		@NotNull
		Integer discountRate,

		@NotNull
		Integer quantity

) {
}
