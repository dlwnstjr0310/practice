package com.study.order.exception.server;

import com.study.order.exception.Error;
import com.study.order.exception.ServerException;
import org.springframework.http.HttpStatus;

public class TimeoutException extends ServerException {
	public TimeoutException() {
		super(Error.SERVER_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT);
	}
}
