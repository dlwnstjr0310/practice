package com.study.member.exception;

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
	NOT_FOUND_WISHLIST(1006, "해당 위시리스트를 찾을 수 없습니다."),
	QUANTITY_NOT_ENOUGH(1007, "수량은 1개 이상이어야 합니다."),

	NOT_FOUND_PRODUCT(2000, "해당 상품을 찾을 수 없습니다."),
	IS_NOT_SALE_PRODUCT(2001, "판매 중인 상품이 아닙니다."),

	INTERNAL_SERVER_ERROR(9999, "서버 오류입니다.");

	Integer code;
	String message;

}
