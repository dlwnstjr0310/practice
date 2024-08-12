package com.study.web.model.response;

public class MemberResponse {

	public record Info(
			String accessToken,
			Long id,
			String email,
			String name
	) {
	}

}
