package com.study.member.exception.product;

import com.study.member.exception.Error;
import com.study.member.exception.ProductException;
import org.springframework.http.HttpStatus;

public class NotFoundProductException extends ProductException {
	public NotFoundProductException() {
		super(Error.NOT_FOUND_PRODUCT, HttpStatus.NOT_FOUND);
	}
}
