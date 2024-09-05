package com.study.order.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDTO(

		@NotNull
		Long memberId,

		@NotBlank
		String addressAlias,

		@NotBlank
		String destinationAddress,

		@NotBlank
		String zipCode,

		@NotBlank
		String phone,

		@NotNull
		Boolean isDefault,

		@NotNull
		Boolean isStoreInAddress,

		@NotNull
		List<ProductOrderRequestDTO> productList
) {
}
