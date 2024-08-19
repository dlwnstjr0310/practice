package com.study.member.model.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReissueTokenRequestDTO(
		@NotNull
		Long id,

		@NotBlank
		String accessToken
) {
}
