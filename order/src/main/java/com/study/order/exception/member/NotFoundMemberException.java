package com.study.order.exception.member;

import com.study.order.exception.Error;
import com.study.order.exception.MemberException;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends MemberException {
	public NotFoundMemberException() {
		super(Error.NOT_FOUND_MEMBER, HttpStatus.NOT_FOUND);
	}
}
