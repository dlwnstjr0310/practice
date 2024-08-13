package com.study.web.model.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueAccessTokenDTO(
		@NotBlank
		Long id,

		@NotBlank
		String accessToken
) {
}
