package com.study.auth.exception.login;

import com.study.auth.exception.Error;
import com.study.auth.exception.LoginException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends LoginException {

	public NotFoundMemberException() {
		super(Error.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND);
	}
}
