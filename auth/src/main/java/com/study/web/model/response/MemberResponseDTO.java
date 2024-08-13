package com.study.web.model.response;

import com.study.web.domain.entity.Member;

public record MemberResponseDTO(
		String accessToken,
		Long id,
		String email,
		String name
) {

	public static MemberResponseDTO of(String accessToken, Member member) {
		return new MemberResponseDTO(
				accessToken,
				member.getId(),
				member.getEmail(),
				member.getName()
		);
	}
}