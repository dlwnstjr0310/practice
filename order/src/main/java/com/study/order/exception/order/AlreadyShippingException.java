package com.study.order.exception.order;

import com.study.order.exception.Error;
import com.study.order.exception.OrderException;
import org.springframework.http.HttpStatus;

public class AlreadyShippingException extends OrderException {
	public AlreadyShippingException() {
		super(Error.ALREADY_SHIPPING, HttpStatus.BAD_REQUEST);
	}
}
