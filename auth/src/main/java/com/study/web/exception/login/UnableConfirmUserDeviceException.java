package com.study.web.exception.login;

import com.study.web.exception.Error;
import com.study.web.exception.LoginException;
import org.springframework.http.HttpStatus;

public class UnableConfirmUserDeviceException extends LoginException {
	public UnableConfirmUserDeviceException() {
		super(Error.UNABLE_CONFIRM_USER_DEVICE, HttpStatus.UNAUTHORIZED);
	}
}
