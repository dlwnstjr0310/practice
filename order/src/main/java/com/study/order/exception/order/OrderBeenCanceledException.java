package com.study.order.exception.order;

import com.study.order.exception.Error;
import com.study.order.exception.OrderException;
import org.springframework.http.HttpStatus;

public class OrderBeenCanceledException extends OrderException {
	public OrderBeenCanceledException() {
		super(Error.ORDER_BEEN_CANCELED, HttpStatus.BAD_REQUEST);
	}
}
