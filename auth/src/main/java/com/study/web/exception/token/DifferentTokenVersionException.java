package com.study.web.exception.token;

import com.study.web.exception.Error;
import com.study.web.exception.TokenException;
import org.springframework.http.HttpStatus;

public class DifferentTokenVersionException extends TokenException {

	String token;

	public DifferentTokenVersionException(String token) {
		super(Error.DIFFERENT_TOKEN_VERSION, HttpStatus.UNAUTHORIZED);
		this.token = token;
	}
}
