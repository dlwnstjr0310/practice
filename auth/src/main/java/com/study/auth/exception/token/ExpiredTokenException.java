package com.study.auth.exception.token;

import com.study.auth.exception.Error;
import com.study.auth.exception.TokenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExpiredTokenException extends TokenException {

	String token;

	public ExpiredTokenException(String token) {
		super(Error.EXPIRED_TOKEN, HttpStatus.UNAUTHORIZED);
		this.token = token;
	}
}
