package com.study.order.model.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record OrderRequestDTO(

		@NotBlank
		Long memberId,

		@NotBlank
		String destinationAddress,

		@NotBlank
		Boolean isDefault,

		@NotBlank
		Boolean isStoreInAddress,
		
		@NotBlank
		List<Long> productIdList
) {
}
