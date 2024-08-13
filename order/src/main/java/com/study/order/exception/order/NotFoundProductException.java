package com.study.order.exception.order;

import com.study.order.exception.Error;
import com.study.order.exception.OrderException;
import org.springframework.http.HttpStatus;

public class NotFoundProductException extends OrderException {
	public NotFoundProductException() {
		super(Error.NOT_FOUND_PRODUCT, HttpStatus.NOT_FOUND);
	}
}
