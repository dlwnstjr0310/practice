package com.study.product.exception.product;

import com.study.product.exception.Error;
import com.study.product.exception.ProductException;
import org.springframework.http.HttpStatus;

public class IsNotSaleProductException extends ProductException {
	public IsNotSaleProductException() {
		super(Error.IS_NOT_SALE_PRODUCT, HttpStatus.BAD_REQUEST);
	}
}
