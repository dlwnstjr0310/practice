package com.study.order.exception.product;

import com.study.order.exception.Error;
import com.study.order.exception.ProductException;
import org.springframework.http.HttpStatus;

public class IsNotSaleProductException extends ProductException {
	public IsNotSaleProductException() {
		super(Error.IS_NOT_SALE_PRODUCT, HttpStatus.BAD_REQUEST);
	}
}
