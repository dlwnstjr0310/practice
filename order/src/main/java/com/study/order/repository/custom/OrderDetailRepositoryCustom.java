package com.study.order.repository.custom;

import com.study.order.model.response.order.OrderResponseDTO;

import java.util.List;

public interface OrderDetailRepositoryCustom {

	List<OrderResponseDTO> findOrderListAndOrderDetailListByMemberId(Long memberId);
}
