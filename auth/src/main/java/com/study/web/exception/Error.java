package com.study.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum Error {

	NOT_FOUND_MEMBER(1000, "존재하지 않는 사용자입니다."),
	ALREADY_EXIST_EMAIL(1001, "이미 존재하는 이메일입니다."),
	NOT_CORRECT_CERTIFICATION_NUMBER(1002, "인증번호가 틀렸습니다."),
	EXPIRED_CERTIFICATION_NUMBER(1003, "인증번호가 만료되었습니다."),
	INVALID_PASSWORD(1004, "비밀번호가 맞지 않습니다."),
	ACCOUNT_NOT_ENABLED(1005, "활성화 되지 않은 계정입니다."),
	EXPIRED_TOKEN(9001, "토큰이 만료되었습니다."),
	INVALID_TOKEN(9002, "잘못된 토큰입니다."),
	UNAUTHORIZED(9003, "로그인이 필요합니다."),
	PERMISSION_DENIED(9004, "권한이 없습니다."),
	INTERNAL_SERVER_ERROR(9999, "서버 오류입니다.");

	Integer code;
	String message;

}
