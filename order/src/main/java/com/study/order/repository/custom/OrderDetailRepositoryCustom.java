package com.study.order.repository.custom;

import com.study.order.model.response.OrderDetailResponseDTO;
import com.study.order.model.response.OrderMemberResponseDTO;

import java.util.List;
import java.util.Map;

public interface OrderDetailRepositoryCustom {

	Map<OrderMemberResponseDTO, List<OrderDetailResponseDTO>> findOrderListAndOrderDetailListByMemberId(Long memberId);
}
