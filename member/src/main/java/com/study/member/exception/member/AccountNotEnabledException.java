package com.study.member.exception.member;

import com.study.member.exception.Error;
import com.study.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class AccountNotEnabledException extends MemberException {
	public AccountNotEnabledException() {
		super(Error.ACCOUNT_NOT_ENABLED, HttpStatus.UNAUTHORIZED);
	}
}
