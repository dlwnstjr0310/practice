package com.study.order.domain.entity.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

	ORDER_COMPLETED("주문 완료"),
	SHIPPING("배송중"),
	SHIPPING_COMPLETED("배송 완료"),
	CANCELED("취소"),
	RETURN_PENDING("반품중"),
	RETURN_COMPLETED("반품 완료"),
	ORDER_FINALIZED("구매 확정");

	private final String description;
}
