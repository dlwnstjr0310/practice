package com.study.web.exception.login;

import com.study.web.exception.Error;
import com.study.web.exception.LoginException;
import org.springframework.http.HttpStatus;

public class ExpiredCertificationNumberException extends LoginException {
	public ExpiredCertificationNumberException() {
		super(Error.EXPIRED_CERTIFICATION_NUMBER, HttpStatus.REQUEST_TIMEOUT);
	}
}
