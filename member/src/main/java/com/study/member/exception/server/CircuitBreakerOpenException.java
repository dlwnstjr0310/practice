package com.study.member.exception.server;

import com.study.member.exception.Error;
import com.study.member.exception.ServerException;
import org.springframework.http.HttpStatus;

public class CircuitBreakerOpenException extends ServerException {
	public CircuitBreakerOpenException() {
		super(Error.CIRCUIT_BREAKER_OPEN, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
