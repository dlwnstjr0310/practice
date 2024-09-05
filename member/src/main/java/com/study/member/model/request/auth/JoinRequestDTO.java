package com.study.member.model.request.auth;

import com.study.member.domain.common.CommonConstant;
import com.study.member.domain.entity.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record JoinRequestDTO(
		@NotBlank
		@Size(max = 50)
		@Pattern(regexp = CommonConstant.RegExp.EMAIL)
		String email,

		@NotBlank
		@Size(min = 8, max = 20)
		String password,

		@NotBlank
		String name,

		@NotBlank
		@Pattern(regexp = CommonConstant.RegExp.PHONE)
		String phone
) {

	public Member toEntity() {

		return Member.builder()
				.email(email)
				.password(password)
				.name(name)
				.phone(phone)
				.build();
	}
}
