package com.study.member.model.response.member;

import com.study.member.domain.entity.member.Member;

public record TokenResponseDTO(

		String tokenType,
		Long expiresIn,
		Long memberId,
		String email,
		String name,
		String role

) {

	public static TokenResponseDTO of(String tokenType,
	                                  Long expiresIn,
	                                  Member member) {
		return new TokenResponseDTO(
				tokenType,
				expiresIn,
				member.getId(),
				member.getEmail(),
				member.getName(),
				member.getMemberRole().getDescription()
		);
	}
}
