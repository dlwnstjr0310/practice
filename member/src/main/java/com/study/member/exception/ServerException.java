package com.study.member.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServerException extends RuntimeException {

	Error error;

	HttpStatus httpstatus;

	public ServerException(Error error, HttpStatus httpStatus) {
		this.error = error;
		this.httpstatus = httpStatus;
	}

}
