package com.study.web.exception.login;

import com.study.web.exception.Error;
import com.study.web.exception.LoginException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends LoginException {
	public InvalidPasswordException() {
		super(Error.INVALID_PASSWORD, HttpStatus.UNAUTHORIZED);
	}
}
