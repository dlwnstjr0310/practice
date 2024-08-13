package com.study.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum Error {

	NOT_FOUND_PRODUCT(2000, "해당 상품을 찾을 수 없습니다."),
	IS_NOT_SALE_PRODUCT(2001, "판매 중인 상품이 아닙니다."),
	INTERNAL_SERVER_ERROR(9999, "서버 오류입니다.");

	Integer code;
	String message;

}
