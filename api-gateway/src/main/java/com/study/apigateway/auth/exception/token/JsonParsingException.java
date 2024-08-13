package com.study.apigateway.auth.exception.token;

import com.study.apigateway.auth.exception.Error;
import com.study.apigateway.auth.exception.TokenException;
import org.springframework.http.HttpStatus;

public class JsonParsingException extends TokenException {
	public JsonParsingException() {
		super(Error.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
