package com.study.order.client;

import com.study.order.model.response.member.MemberResponseDTO;
import com.study.order.model.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service", url = "${service.url.member}")
public interface MemberClient {

	@GetMapping("/member/{id}")
	Response<MemberResponseDTO> getMemberInfo(@PathVariable Long id);
}
