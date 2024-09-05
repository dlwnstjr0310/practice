package com.study.member.exception.member;

import com.study.member.exception.Error;
import com.study.member.exception.MemberException;
import org.springframework.http.HttpStatus;

public class NotFoundWishListException extends MemberException {
	public NotFoundWishListException() {
		super(Error.NOT_FOUND_WISHLIST, HttpStatus.NOT_FOUND);
	}
}
