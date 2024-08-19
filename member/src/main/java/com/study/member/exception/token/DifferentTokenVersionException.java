package com.study.member.exception.token;

import com.study.member.exception.Error;
import com.study.member.exception.TokenException;
import org.springframework.http.HttpStatus;

public class DifferentTokenVersionException extends TokenException {

	String token;

	public DifferentTokenVersionException(String token) {
		super(Error.DIFFERENT_TOKEN_VERSION, HttpStatus.UNAUTHORIZED);
		this.token = token;
	}
}
