package com.study.member.exception.product;

import com.study.member.exception.Error;
import com.study.member.exception.ProductException;
import org.springframework.http.HttpStatus;

public class IsNotSaleProductException extends ProductException {
	public IsNotSaleProductException() {
		super(Error.IS_NOT_SALE_PRODUCT, HttpStatus.BAD_REQUEST);
	}
}
