package com.study.order.exception.order;

import com.study.order.exception.Error;
import com.study.order.exception.OrderException;
import org.springframework.http.HttpStatus;

public class OutOfStockException extends OrderException {
	public OutOfStockException() {
		super(Error.OUT_OF_STOCK, HttpStatus.BAD_REQUEST);
	}
}
