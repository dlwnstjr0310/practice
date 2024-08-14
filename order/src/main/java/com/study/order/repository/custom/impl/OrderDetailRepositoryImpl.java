package com.study.order.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.order.model.response.OrderDetailResponseDTO;
import com.study.order.model.response.OrderMemberResponseDTO;
import com.study.order.repository.custom.OrderDetailRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.study.order.domain.entity.QOrderDetail.orderDetail;
import static com.study.order.domain.entity.order.QOrder.order;

@RequiredArgsConstructor
public class OrderDetailRepositoryImpl implements OrderDetailRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public Map<OrderMemberResponseDTO, List<OrderDetailResponseDTO>> findOrderListAndOrderDetailListByMemberId(Long memberId) {

		//todo: 여기 duplicate key 해결하기
		return queryFactory.selectFrom(orderDetail)
				.leftJoin(orderDetail.order, order)
				.fetchJoin()
				.where(order.memberId.eq(memberId))
				.fetch()
				.stream()
				.collect(Collectors.toMap(
						orderDetail -> OrderMemberResponseDTO.of(orderDetail.getOrder()),
						orderDetail -> List.of(OrderDetailResponseDTO.of(orderDetail))
				));
	}
}
