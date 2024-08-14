package com.study.order.exception.product;

import com.study.order.exception.Error;
import com.study.order.exception.ProductException;
import org.springframework.http.HttpStatus;

public class NotFoundProductException extends ProductException {
	public NotFoundProductException() {
		super(Error.NOT_FOUND_PRODUCT, HttpStatus.NOT_FOUND);
	}
}
