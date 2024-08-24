package com.study.order.controller;

import com.study.order.controller.docs.OrderMemberControllerDocs;
import com.study.order.model.response.order.OrderResponseDTO;
import com.study.order.service.OrderMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order/member")
public class OrderMemberController implements OrderMemberControllerDocs {

	private final OrderMemberService orderMemberService;

	@GetMapping("/{id}")
	public List<OrderResponseDTO> getMemberOrderList(@PathVariable Long id) {

		return orderMemberService.getMemberOrderList(id);

	}
}
