package com.study.auth.exception.login;

import com.study.auth.exception.Error;
import com.study.auth.exception.LoginException;
import org.springframework.http.HttpStatus;

public class ExpiredCertificationNumberException extends LoginException {
	public ExpiredCertificationNumberException() {
		super(Error.EXPIRED_CERTIFICATION_NUMBER, HttpStatus.REQUEST_TIMEOUT);
	}
}
