package com.study.auth.exception.login;

import com.study.auth.exception.Error;
import com.study.auth.exception.LoginException;
import org.springframework.http.HttpStatus;

public class AlreadyExistEmailException extends LoginException {
	public AlreadyExistEmailException() {
		super(Error.ALREADY_EXIST_EMAIL, HttpStatus.CONFLICT);
	}
}
