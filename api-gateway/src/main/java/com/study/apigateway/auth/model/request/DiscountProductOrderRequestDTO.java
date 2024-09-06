package com.study.apigateway.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DiscountProductOrderRequestDTO(

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
		ProductOrderRequestDTO product

) {
}
