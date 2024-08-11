package com.study.web.model.response;

public class LoginResponse {

	public record Info(
			String accessToken,
			Long id,
			String email,
			String name
	) {
	}

}
