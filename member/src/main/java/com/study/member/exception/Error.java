package com.study.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum Error {

	NOT_FOUND_MEMBER(1000, "존재하지 않는 사용자입니다."),
	
	INTERNAL_SERVER_ERROR(9999, "서버 오류입니다.");

	Integer code;
	String message;

}
