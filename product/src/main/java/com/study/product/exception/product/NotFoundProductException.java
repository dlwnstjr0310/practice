package com.study.product.exception.product;

import com.study.product.exception.Error;
import com.study.product.exception.ProductException;
import org.springframework.http.HttpStatus;

public class NotFoundProductException extends ProductException {
	public NotFoundProductException() {
		super(Error.NOT_FOUND_PRODUCT, HttpStatus.NOT_FOUND);
	}
}
