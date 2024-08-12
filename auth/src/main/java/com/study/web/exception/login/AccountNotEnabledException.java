package com.study.web.exception.login;

import com.study.web.exception.Error;
import com.study.web.exception.LoginException;
import org.springframework.http.HttpStatus;

public class AccountNotEnabledException extends LoginException {
	public AccountNotEnabledException() {
		super(Error.ACCOUNT_NOT_ENABLED, HttpStatus.UNAUTHORIZED);
	}
}
