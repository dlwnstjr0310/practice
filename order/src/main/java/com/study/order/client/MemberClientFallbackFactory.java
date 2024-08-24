package com.study.order.client;

import com.study.order.domain.event.producer.AddressEvent;
import com.study.order.model.response.Response;
import com.study.order.model.response.member.MemberResponseDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class MemberClientFallbackFactory implements FallbackFactory<MemberClient> {

	@Override
	public MemberClient create(Throwable cause) {
		return new MemberClient() {
			//todo: 추가 설정 해야함
			@Override
			public Response<MemberResponseDTO> getMemberInfo(Long id) {

				if (cause instanceof CallNotPermittedException) {
					return Response.<MemberResponseDTO>builder()
							.code(HttpStatus.SC_SERVICE_UNAVAILABLE)
							.build();
				} else if (cause instanceof TimeoutException) {
					return Response.<MemberResponseDTO>builder()
							.code(HttpStatus.SC_GATEWAY_TIMEOUT)
							.build();
				} else {
					return Response.<MemberResponseDTO>builder()
							.code(HttpStatus.SC_INTERNAL_SERVER_ERROR)
							.build();
				}
			}

			@Override
			public Response<Void> updateAddress(AddressEvent request) {
				if (cause instanceof CallNotPermittedException) {
					return Response.<Void>builder()
							.code(HttpStatus.SC_SERVICE_UNAVAILABLE)
							.build();
				} else if (cause instanceof TimeoutException) {
					return Response.<Void>builder()
							.code(HttpStatus.SC_GATEWAY_TIMEOUT)
							.build();
				} else {
					return Response.<Void>builder()
							.code(HttpStatus.SC_INTERNAL_SERVER_ERROR)
							.build();
				}
			}
		};
	}
}
