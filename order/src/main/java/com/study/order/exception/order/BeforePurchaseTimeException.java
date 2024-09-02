package com.study.order.exception.order;

import com.study.order.exception.Error;
import com.study.order.exception.OrderException;
import org.springframework.http.HttpStatus;

public class BeforePurchaseTimeException extends OrderException {
	public BeforePurchaseTimeException() {
		super(Error.BEFORE_PURCHASE_TIME, HttpStatus.BAD_REQUEST);
	}
}
