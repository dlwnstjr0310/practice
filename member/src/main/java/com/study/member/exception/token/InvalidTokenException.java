package com.study.member.exception.token;

import com.study.member.exception.Error;
import com.study.member.exception.TokenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidTokenException extends TokenException {

	String token;

	public InvalidTokenException(String token) {
		super(Error.INVALID_TOKEN, HttpStatus.UNAUTHORIZED);
		this.token = token;
	}
}
