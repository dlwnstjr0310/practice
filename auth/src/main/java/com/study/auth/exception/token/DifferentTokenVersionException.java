package com.study.auth.exception.token;

import com.study.auth.exception.Error;
import com.study.auth.exception.TokenException;
import org.springframework.http.HttpStatus;

public class DifferentTokenVersionException extends TokenException {

	String token;

	public DifferentTokenVersionException(String token) {
		super(Error.DIFFERENT_TOKEN_VERSION, HttpStatus.UNAUTHORIZED);
		this.token = token;
	}
}
