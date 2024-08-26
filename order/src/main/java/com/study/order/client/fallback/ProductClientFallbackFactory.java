package com.study.order.client.fallback;

import com.study.order.client.ProductClient;
import com.study.order.exception.order.OutOfStockException;
import com.study.order.exception.product.IsNotSaleProductException;
import com.study.order.exception.server.CircuitBreakerOpenException;
import com.study.order.exception.server.GatewayTimeoutException;
import com.study.order.model.request.ProductOrderRequestDTO;
import com.study.order.model.response.order.ProductOrderResponseDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
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
			public List<ProductOrderResponseDTO> modifyProductStock(List<ProductOrderRequestDTO> productList) {
				if (cause instanceof OutOfStockException) {
					throw new OutOfStockException();
				} else if (cause instanceof CallNotPermittedException) {
					throw new CircuitBreakerOpenException();
				} else if (cause instanceof TimeoutException) {
					throw new GatewayTimeoutException();
				} else {
					throw new InternalServerErrorException();
				}
			}

			@Override
			public List<ProductOrderResponseDTO> getProductOrderList(List<Long> productIdList) {

				if (cause instanceof IsNotSaleProductException) {
					throw new IsNotSaleProductException();
				} else if (cause instanceof CallNotPermittedException) {
					throw new CircuitBreakerOpenException();
				} else if (cause instanceof TimeoutException) {
					throw new GatewayTimeoutException();
				} else {
					throw new InternalServerErrorException();
				}
			}
		};
	}
}
