package com.study.web.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginException extends RuntimeException {

	Error error;

	HttpStatus httpStatus;

	public LoginException(Error error, HttpStatus httpStatus) {
		this.error = error;
		this.httpStatus = httpStatus;
	}

}
