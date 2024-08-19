package com.study.member.exception.member;

import com.study.member.exception.Error;
import com.study.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class ExpiredCertificationNumberException extends MemberException {
	public ExpiredCertificationNumberException() {
		super(Error.EXPIRED_CERTIFICATION_NUMBER, HttpStatus.REQUEST_TIMEOUT);
	}
}
