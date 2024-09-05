package com.study.member.model.request.auth;

import com.study.member.domain.common.CommonConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
		@NotBlank
		@Size(max = 50)
		@Pattern(regexp = CommonConstant.RegExp.EMAIL)
		String email,

		@NotBlank
		@Size(min = 8, max = 20)
		String password
) {
}
