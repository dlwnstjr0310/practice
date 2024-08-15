package com.study.member.client;

import com.study.member.model.response.order.OrderResponseDTO;
import com.study.member.model.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service", url = "${service.url.order}")
public interface OrderClient {

	@GetMapping("/order/member/{id}")
	Response<List<OrderResponseDTO>> getMemberOrderList(@PathVariable Long id);
}
