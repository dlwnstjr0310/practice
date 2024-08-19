package com.study.member.exception.member;

import com.study.member.exception.Error;
import com.study.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class NotCorrectCertificationNumberException extends MemberException {
	public NotCorrectCertificationNumberException() {
		super(Error.NOT_CORRECT_CERTIFICATION_NUMBER, HttpStatus.BAD_REQUEST);
	}
}
