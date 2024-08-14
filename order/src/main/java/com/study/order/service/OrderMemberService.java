package com.study.order.service;

import com.study.order.model.response.OrderDetailResponseDTO;
import com.study.order.model.response.OrderMemberResponseDTO;
import com.study.order.repository.OrderDetailRepository;
import com.study.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderMemberService {

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;

	@Transactional(readOnly = true)
	public Map<OrderMemberResponseDTO, List<OrderDetailResponseDTO>> getMemberOrderList(Long id) {

		return orderDetailRepository.findOrderListAndOrderDetailListByMemberId(id);
	}
}
