package com.study.order.exception.server;

import com.study.order.exception.Error;
import com.study.order.exception.ServerException;
import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ServerException {
	public ServiceUnavailableException() {
		super(Error.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
	}
}
