package com.study.member.exception.member;

import com.study.member.exception.Error;
import com.study.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends MemberException {
	public NotFoundMemberException() {
		super(Error.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND);
	}
}
