package com.study.member.model.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LogoutRequestDTO(
		@NotNull
		Long id,

		@NotBlank
		String accessToken,

		@NotNull
		Boolean isAllDevice
) {
}
