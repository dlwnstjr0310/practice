package com.study.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReissueAccessTokenDTO(
		@NotNull
		Long id,

		@NotBlank
		String accessToken
) {
}
