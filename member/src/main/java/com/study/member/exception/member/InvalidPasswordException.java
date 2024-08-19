package com.study.member.exception.member;

import com.study.member.exception.Error;
import com.study.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends MemberException {
	public InvalidPasswordException() {
		super(Error.INVALID_PASSWORD, HttpStatus.UNAUTHORIZED);
	}
}
