package com.study.order.client;

import com.study.order.client.fallback.MemberClientFallbackFactory;
import com.study.order.domain.event.producer.AddressEvent;
import com.study.order.model.response.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "member")
@FeignClient(name = "member-service", url = "${service.url.member}", fallbackFactory = MemberClientFallbackFactory.class)
public interface MemberClient {

	@PostMapping("/member/address")
	Response<Void> updateAddress(@RequestBody AddressEvent request);
}
