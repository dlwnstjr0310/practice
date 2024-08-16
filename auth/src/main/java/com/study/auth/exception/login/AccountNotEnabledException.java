package com.study.auth.exception.login;

import com.study.auth.exception.Error;
import com.study.auth.exception.LoginException;
import org.springframework.http.HttpStatus;

public class AccountNotEnabledException extends LoginException {
	public AccountNotEnabledException() {
		super(Error.ACCOUNT_NOT_ENABLED, HttpStatus.UNAUTHORIZED);
	}
}
