package com.study.web.exception.login;

import com.study.web.exception.Error;
import com.study.web.exception.LoginException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends LoginException {

	public NotFoundMemberException() {
		super(Error.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND);
	}
}
