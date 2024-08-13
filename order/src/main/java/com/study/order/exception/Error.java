package com.study.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum Error {

	NOT_FOUND_ORDER(2000, "해당 주문을 찾을 수 없습니다."),
	NOT_FOUND_PRODUCT(2001, "해당 상품을 찾을 수 없습니다."),
	IS_NOT_SALE_PRODUCT(2002, "판매 중인 상품이 아닙니다."),
	OUT_OF_STOCK(2003, "재고가 부족합니다."),
	ALREADY_SHIPPING(2004, "이미 배송 중인 상품입니다."),
	RETURN_PERIOD_PASSED(2005, "반품 기간이 지났습니다."),
	INTERNAL_SERVER_ERROR(9999, "서버 오류입니다.");

	Integer code;
	String message;

}
