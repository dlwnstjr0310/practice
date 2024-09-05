package com.study.member.exception.member;

import com.study.member.exception.Error;
import com.study.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class QuantityNotEnoughException extends MemberException {
	public QuantityNotEnoughException() {
		super(Error.QUANTITY_NOT_ENOUGH, HttpStatus.BAD_REQUEST);
	}
}
