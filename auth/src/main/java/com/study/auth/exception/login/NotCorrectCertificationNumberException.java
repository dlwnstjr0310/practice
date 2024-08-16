package com.study.auth.exception.login;

import com.study.auth.exception.Error;
import com.study.auth.exception.LoginException;
import org.springframework.http.HttpStatus;

public class NotCorrectCertificationNumberException extends LoginException {
	public NotCorrectCertificationNumberException() {
		super(Error.NOT_CORRECT_CERTIFICATION_NUMBER, HttpStatus.BAD_REQUEST);
	}
}
