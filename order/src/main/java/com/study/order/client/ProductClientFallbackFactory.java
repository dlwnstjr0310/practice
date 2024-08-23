package com.study.order.client;

import com.study.order.model.request.ProductOrderRequestDTO;
import com.study.order.model.response.Response;
import com.study.order.model.response.order.ProductOrderResponseDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {
	@Override
	public ProductClient create(Throwable cause) {
		return new ProductClient() {

			@Override
			public Response<List<ProductOrderResponseDTO>> modifyProductStock(List<ProductOrderRequestDTO> productList) {
				log.info("서킷브레이커 실행 - productFallbackFactory 호출1", cause);

				if (cause instanceof CallNotPermittedException) {
					log.error("Connection refused");
					return Response.<List<ProductOrderResponseDTO>>builder()
							.code(HttpStatus.SC_SERVICE_UNAVAILABLE)
							.build();
				} else if (cause instanceof TimeoutException) {
					log.error("Timeout occurred");
					return Response.<List<ProductOrderResponseDTO>>builder()
							.code(HttpStatus.SC_GATEWAY_TIMEOUT)
							.build();
				} else {
					log.error("Unexpected error");
					return Response.<List<ProductOrderResponseDTO>>builder()
							.code(HttpStatus.SC_INTERNAL_SERVER_ERROR)
							.build();
				}
			}

			@Override
			public Response<List<ProductOrderResponseDTO>> getProductOrderList(List<Long> productIdList) {
				log.info("서킷브레이커 실행 - productFallbackFactory 호출2", cause);
				if (cause instanceof CallNotPermittedException) {
//					log.error("Connection refused: ", cause);
					log.error("Connection refused");
					return Response.<List<ProductOrderResponseDTO>>builder()
							.code(HttpStatus.SC_SERVICE_UNAVAILABLE)
							.build();
				} else if (cause instanceof TimeoutException) {
//					log.error("Timeout occurred: ", cause);
					log.error("Timeout occurred");
					return Response.<List<ProductOrderResponseDTO>>builder()
							.code(HttpStatus.SC_GATEWAY_TIMEOUT)
							.build();
				} else {
//					log.error("Unexpected error: ", cause);
					log.error("Unexpected error");
					return Response.<List<ProductOrderResponseDTO>>builder()
							.code(HttpStatus.SC_INTERNAL_SERVER_ERROR)
							.build();
				}
			}
		};
	}
}
