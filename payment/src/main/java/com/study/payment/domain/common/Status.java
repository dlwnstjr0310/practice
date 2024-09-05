package com.study.payment.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

	ORDER_PROGRESS("주문 진행중"),
	ORDER_COMPLETED("주문 완료"),
	ORDER_CANCELED("취소"),
	PAYMENT_COMPLETED("결제 완료"),
	PAYMENT_FAILED("결제 실패"),
	SHIPPING("배송중"),
	SHIPPING_COMPLETED("배송 완료"),
	RETURN_PENDING("반품중"),
	RETURN_COMPLETED("반품 완료"),
	ORDER_FINALIZED("구매 확정");

	private final String description;
}
