package com.study.order.client;

import com.study.order.model.response.MemberResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service", url = "${service.url.member}")
public interface MemberClient {

	@GetMapping("/member/{id}")
	MemberResponseDTO getMemberInfo(@PathVariable Long id);
}
