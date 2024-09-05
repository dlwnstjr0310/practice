package com.study.apigateway.auth.exception.token;

import com.study.apigateway.auth.exception.Error;
import com.study.apigateway.auth.exception.TokenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisteredInBlackListException extends TokenException {
	String token;

	public RegisteredInBlackListException(String token) {
		super(Error.REGISTERED_IN_BLACKLIST, HttpStatus.UNAUTHORIZED);
		this.token = token;
	}
}
