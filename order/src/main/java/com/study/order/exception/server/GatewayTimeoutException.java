package com.study.order.exception.server;

import com.study.order.exception.Error;
import com.study.order.exception.ServerException;
import org.springframework.http.HttpStatus;

public class GatewayTimeoutException extends ServerException {
	public GatewayTimeoutException() {
		super(Error.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT);
	}
}
