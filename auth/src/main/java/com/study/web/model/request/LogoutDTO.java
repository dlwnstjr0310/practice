package com.study.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LogoutDTO(
		@NotBlank
		Long id,

		@NotBlank
		String accessToken,

		@NotNull
		Boolean isAllDevice
) {
}
