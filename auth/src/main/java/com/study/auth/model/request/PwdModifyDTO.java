package com.study.auth.model.request;

import com.study.auth.domain.common.CommonConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PwdModifyDTO(

		@NotBlank
		@Size(max = 50)
		@Pattern(regexp = CommonConstant.RegExp.EMAIL)
		String email,

		@NotBlank
		String oldPwd,

		@NotBlank
		@Size(min = 8, max = 20)
		String newPwd

) {
}
