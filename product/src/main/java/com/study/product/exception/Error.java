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
	NOT_CORRECT_CATEGORY(2002, "정확한 카테고리를 입력해주세요."),

	OUT_OF_STOCK(3001, "재고가 부족합니다."),

	INTERNAL_SERVER_ERROR(9999, "서버 오류입니다.");

	Integer code;
	String message;

}
