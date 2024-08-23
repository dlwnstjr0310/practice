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
			@Override
			public Response<MemberResponseDTO> getMemberInfo(Long id) {

				if (cause instanceof CallNotPermittedException) {
//					log.error("Connection refused: ", cause);
					log.error("Connection refused");
					return Response.<MemberResponseDTO>builder()
							.code(HttpStatus.SC_SERVICE_UNAVAILABLE)
							.build();
				} else if (cause instanceof TimeoutException) {
//					log.error("Timeout occurred: ", cause);
					log.error("Timeout occurred");
					return Response.<MemberResponseDTO>builder()
							.code(HttpStatus.SC_GATEWAY_TIMEOUT)
							.build();
				} else {
//					log.error("Unexpected error: ", cause);
					log.error("Unexpected error");
					return Response.<MemberResponseDTO>builder()
							.code(HttpStatus.SC_INTERNAL_SERVER_ERROR)
							.build();
				}
			}

			@Override
			public Response<Void> updateAddress(AddressEvent request) {
				if (cause instanceof CallNotPermittedException) {
//					log.error("Connection refused: ", cause);
					log.error("Connection refused");
					return Response.<Void>builder()
							.code(HttpStatus.SC_SERVICE_UNAVAILABLE)
							.build();
				} else if (cause instanceof TimeoutException) {
//					log.error("Timeout occurred: ", cause);
					log.error("Timeout occurred");
					return Response.<Void>builder()
							.code(HttpStatus.SC_GATEWAY_TIMEOUT)
							.build();
				} else {
//					log.error("Unexpected error: ", cause);
					log.error("Unexpected error");
					return Response.<Void>builder()
							.code(HttpStatus.SC_INTERNAL_SERVER_ERROR)
							.build();
				}
			}
		};
	}
}
