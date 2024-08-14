package com.study.product.exception.order;

import com.study.product.exception.Error;
import com.study.product.exception.OrderException;
import org.springframework.http.HttpStatus;

public class OutOfStockException extends OrderException {
	public OutOfStockException() {
		super(Error.OUT_OF_STOCK, HttpStatus.BAD_REQUEST);
	}
}
