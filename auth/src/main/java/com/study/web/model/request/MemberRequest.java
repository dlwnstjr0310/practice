package com.study.web.model.request;

import com.study.web.domain.common.CommonConstant;
import com.study.web.domain.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MemberRequest {

	public record Join(
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

	public record Verify(
			@NotBlank
			@Size(max = 50)
			@Pattern(regexp = CommonConstant.RegExp.EMAIL)
			String email,

			@NotBlank
			String certificationText
	) {
	}

	public record Login(
			@NotBlank
			@Size(max = 50)
			@Pattern(regexp = CommonConstant.RegExp.EMAIL)
			String email,

			@NotBlank
			@Size(min = 8, max = 20)
			String password
	) {
	}

	public record Logout(
			@NotBlank
			Long id,

			@NotBlank
			String accessToken,
			
			@NotNull
			Boolean isAllDevice
	) {
	}
}
