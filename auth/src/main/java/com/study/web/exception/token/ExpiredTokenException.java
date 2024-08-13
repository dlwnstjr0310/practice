package com.study.web.exception.token;

import com.study.web.exception.Error;
import com.study.web.exception.TokenException;
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
