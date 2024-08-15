package com.study.member.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductResponseDTO(

		@NotNull
		Long id,

		@NotBlank
		String name,

		@NotNull
		Integer price,

		@NotNull
		Integer stock,

		@NotNull
		Boolean isVisible
) {
}
