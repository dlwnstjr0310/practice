package com.study.web.exception.login;

import com.study.web.exception.Error;
import com.study.web.exception.LoginException;
import org.springframework.http.HttpStatus;

public class NotCorrectCertificationNumberException extends LoginException {
	public NotCorrectCertificationNumberException() {
		super(Error.NOT_CORRECT_CERTIFICATION_NUMBER, HttpStatus.BAD_REQUEST);
	}
}
