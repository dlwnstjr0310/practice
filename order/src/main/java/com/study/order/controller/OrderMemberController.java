package com.study.order.controller;

import com.study.order.controller.docs.OrderMemberControllerDocs;
import com.study.order.model.response.OrderDetailResponseDTO;
import com.study.order.model.response.OrderMemberResponseDTO;
import com.study.order.model.response.Response;
import com.study.order.service.OrderMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order/member")
public class OrderMemberController implements OrderMemberControllerDocs {

	private final OrderMemberService orderMemberService;

	@GetMapping("/{id}")
	public Response<Map<OrderMemberResponseDTO, List<OrderDetailResponseDTO>>> getMemberOrderList(@PathVariable Long id) {

		return Response.<Map<OrderMemberResponseDTO, List<OrderDetailResponseDTO>>>builder()
				.data(orderMemberService.getMemberOrderList(id))
				.build();
	}
}
