package com.study.order.exception.order;

import com.study.order.exception.Error;
import com.study.order.exception.OrderException;
import org.springframework.http.HttpStatus;

public class IsNotSaleProductException extends OrderException {
	public IsNotSaleProductException() {
		super(Error.IS_NOT_SALE_PRODUCT, HttpStatus.BAD_REQUEST);
	}
}
