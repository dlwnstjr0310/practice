package com.study.order.exception.order;

import com.study.order.exception.Error;
import com.study.order.exception.OrderException;
import org.springframework.http.HttpStatus;

public class NotFoundOrderException extends OrderException {
	public NotFoundOrderException() {
		super(Error.NOT_FOUND_ORDER, HttpStatus.NOT_FOUND);
	}
}
