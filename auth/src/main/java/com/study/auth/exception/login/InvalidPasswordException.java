package com.study.auth.exception.login;

import com.study.auth.exception.Error;
import com.study.auth.exception.LoginException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends LoginException {
	public InvalidPasswordException() {
		super(Error.INVALID_PASSWORD, HttpStatus.UNAUTHORIZED);
	}
}
