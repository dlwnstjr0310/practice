package com.study.order.service;

import com.study.order.model.response.order.OrderResponseDTO;
import com.study.order.repository.OrderDetailRepository;
import com.study.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderMemberService {

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;

	@Transactional(readOnly = true)
	public List<OrderResponseDTO> getMemberOrderList(Long id) {

		return orderDetailRepository.findOrderListAndOrderDetailListByMemberId(id);
	}
}
