package com.study.member.exception.member;

import com.study.member.exception.Error;
import com.study.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class AlreadyExistEmailException extends MemberException {
	public AlreadyExistEmailException() {
		super(Error.ALREADY_EXIST_EMAIL, HttpStatus.CONFLICT);
	}
}
