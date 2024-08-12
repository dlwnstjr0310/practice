package com.study.apigateway.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {

	EXPIRED_TOKEN(9000, "GateWay : 토큰이 만료되었습니다."),
	INVALID_TOKEN(9001, "GateWay : 잘못된 토큰입니다."),
	UNAUTHORIZED(9003, "GateWay : 인증되지 않은 사용자입니다."),
	PERMISSION_DENIED(9004, "GateWay : 권한이 없습니다."),
	INTERNAL_SERVER_ERROR(9999, "GateWay : 서버 오류입니다.");

	final Integer code;
	final String message;
}
