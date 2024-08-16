package com.study.auth.model.request;

import com.study.auth.domain.common.CommonConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerifyDTO(
		@NotBlank
		@Size(max = 50)
		@Pattern(regexp = CommonConstant.RegExp.EMAIL)
		String email,

		@NotBlank
		String certificationText
) {
}
