package com.study.order.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.order.model.response.OrderResponseDTO;
import com.study.order.repository.custom.OrderDetailRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.study.order.domain.entity.QOrderDetail.orderDetail;
import static com.study.order.domain.entity.order.QOrder.order;

@RequiredArgsConstructor
public class OrderDetailRepositoryImpl implements OrderDetailRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public List<OrderResponseDTO> findOrderListAndOrderDetailListByMemberId(Long memberId) {

		return queryFactory.selectFrom(orderDetail)
				.leftJoin(orderDetail.order, order)
				.fetchJoin()
				.where(order.memberId.eq(memberId))
				.fetch()
				.stream()
				.collect(Collectors.groupingBy(orderDetail -> orderDetail.getOrder().getId()))
				.values()
				.stream()
				.map(orderDetailList -> OrderResponseDTO.of(orderDetailList.get(0).getOrder(), orderDetailList))
				.toList();
	}
}
