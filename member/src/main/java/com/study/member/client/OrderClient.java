package com.study.member.client;

import com.study.member.model.response.OrderDetailResponseDTO;
import com.study.member.model.response.OrderMemberResponseDTO;
import com.study.member.model.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(name = "order-service", url = "${service.url.order}")
public interface OrderClient {

	@GetMapping("/order/{id}")
	Response<Map<OrderMemberResponseDTO, List<OrderDetailResponseDTO>>> getMemberOrderList(@PathVariable Long id);
}
