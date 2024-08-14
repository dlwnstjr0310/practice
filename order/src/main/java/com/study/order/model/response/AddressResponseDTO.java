package com.study.order.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressResponseDTO(

		@NotNull
		Long id,

		@NotBlank
		String name,

		@NotBlank
		String address,

		@NotBlank
		String zipCode,

		@NotBlank
		String phone,

		@NotNull
		Boolean isDefault
) {
}
