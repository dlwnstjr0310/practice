package com.study.apigateway.auth.exception.token;

import com.study.apigateway.auth.exception.Error;
import com.study.apigateway.auth.exception.TokenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DifferentTokenVersionException extends TokenException {

	String token;

	public DifferentTokenVersionException(String token) {
		super(Error.DIFFERENT_TOKEN_VERSION, HttpStatus.FORBIDDEN);
		this.token = token;
	}
}
