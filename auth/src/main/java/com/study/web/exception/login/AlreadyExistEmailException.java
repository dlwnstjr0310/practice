package com.study.web.exception.login;

import com.study.web.exception.Error;
import com.study.web.exception.LoginException;
import org.springframework.http.HttpStatus;

public class AlreadyExistEmailException extends LoginException {
	public AlreadyExistEmailException() {
		super(Error.ALREADY_EXIST_EMAIL, HttpStatus.CONFLICT);
	}
}
