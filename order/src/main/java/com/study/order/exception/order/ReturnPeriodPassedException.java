package com.study.order.exception.order;

import com.study.order.exception.Error;
import com.study.order.exception.OrderException;
import org.springframework.http.HttpStatus;

public class ReturnPeriodPassedException extends OrderException {
	public ReturnPeriodPassedException() {
		super(Error.RETURN_PERIOD_PASSED, HttpStatus.BAD_REQUEST);
	}
}
