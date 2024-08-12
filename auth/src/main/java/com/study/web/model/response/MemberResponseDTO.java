package com.study.web.model.response;

public record MemberResponseDTO(
		String accessToken,
		Long id,
		String email,
		String name
) {
}