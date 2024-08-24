package com.study.order.exception.server;

import com.study.order.exception.Error;
import com.study.order.exception.ServerException;
import org.springframework.http.HttpStatus;

public class CircuitBreakerOpenException extends ServerException {
	public CircuitBreakerOpenException() {
		super(Error.CIRCUIT_BREAKER_OPEN, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
