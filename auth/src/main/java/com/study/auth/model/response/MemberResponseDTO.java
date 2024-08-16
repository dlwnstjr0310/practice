package com.study.auth.model.response;

import com.study.auth.domain.entity.Member;

public record MemberResponseDTO(

		String tokenType,
		Long expiresIn,
		Long memberId,
		String email,
		String name,
		String role
) {

	public static MemberResponseDTO of(String tokenType,
	                                   Long expiresIn,
	                                   Member member) {
		return new MemberResponseDTO(
				tokenType,
				expiresIn,
				member.getId(),
				member.getEmail(),
				member.getName(),
				member.getMemberRole().getDescription()
		);
	}
}