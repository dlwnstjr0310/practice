package com.study.order.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequestDTO(

		@NotNull
		Long memberId,

		@NotBlank
		String destinationAddress,

		@NotNull
		Boolean isDefault,

		@NotNull
		Boolean isStoreInAddress,

		@NotNull
		List<ProductOrderRequestDTO> productList
) {
}
