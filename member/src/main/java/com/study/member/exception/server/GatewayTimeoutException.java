package com.study.member.exception.server;

import com.study.member.exception.Error;
import com.study.member.exception.ServerException;
import org.springframework.http.HttpStatus;

public class GatewayTimeoutException extends ServerException {
	public GatewayTimeoutException() {
		super(Error.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT);
	}
}
