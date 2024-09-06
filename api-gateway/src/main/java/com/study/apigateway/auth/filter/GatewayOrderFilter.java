package com.study.apigateway.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.apigateway.auth.model.request.DiscountProductOrderRequestDTO;
import com.study.apigateway.service.QueueEventProducer;
import com.study.apigateway.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class GatewayOrderFilter extends AbstractGatewayFilterFactory<GatewayOrderFilter.Config> {

	private static final String PATH = "/order/discount";

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RedisService redisService;
	private final QueueEventProducer queueEventProducer;

	public GatewayOrderFilter(RedisService redisService, QueueEventProducer queueEventProducer) {
		super(GatewayOrderFilter.Config.class);
		this.redisService = redisService;
		this.queueEventProducer = queueEventProducer;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			if (request.getPath().value().equals(PATH)) {
				return handleOrderRequest(exchange);
			}

			return chain.filter(exchange);
		};
	}

	private Mono<Void> handleOrderRequest(ServerWebExchange exchange) {
		return exchange.getRequest().getBody().collectList().flatMap(dataBuffers -> {
			try {
				StringBuilder bodyBuilder = new StringBuilder();
				dataBuffers.forEach(buffer -> {
					byte[] bytes = new byte[buffer.readableByteCount()];
					buffer.read(bytes);
					DataBufferUtils.release(buffer);
					bodyBuilder.append(new String(bytes, StandardCharsets.UTF_8));
				});

				String bodyString = bodyBuilder.toString();
				DiscountProductOrderRequestDTO orderRequest = objectMapper.readValue(bodyString, DiscountProductOrderRequestDTO.class);

				// Redis에 대기열 정보 저장
				String memberId = orderRequest.memberId().toString();
				redisService.storeZset(memberId);

				// Kafka로 create-queue 이벤트 발행
				queueEventProducer.sendQueueEvent(orderRequest);

				// SSE 응답 설정
				ServerHttpResponse response = exchange.getResponse();
				response.getHeaders().setContentType(MediaType.TEXT_EVENT_STREAM);

				return response.writeWith(createSseStream(
						memberId,
						orderRequest.product().productId().toString(),
						exchange
				));
			} catch (IOException e) {
				return Mono.error(new RuntimeException("Failed to process request", e));
			}
		});
	}

	private Flux<DataBuffer> createSseStream(String memberId, String productId, ServerWebExchange exchange) {
		return Flux.create(emitter -> {
			ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			AtomicBoolean isCompleted = new AtomicBoolean(false);
			AtomicInteger retryCount = new AtomicInteger(0);
			int maxRetries = 3;

			Runnable task = () -> {
				if (!isCompleted.get()) {
					Long waitingCount = redisService.getRank(memberId);
					String orderStatus = redisService.getOrderStatus(memberId + "|" + productId);

					// 주문 상태가 존재하면 종료
					if (orderStatus != null) {
						String eventData = "data: 주문 상태 : " + orderStatus + "\n\n";
						emitter.next(exchange.getResponse().bufferFactory().wrap(eventData.getBytes(StandardCharsets.UTF_8)));
						emitter.complete();
						isCompleted.set(true);
						executor.shutdown();
					} else if (waitingCount > 0) {
						// 최신 순위 정보만 전송
						String eventData = "data: 현재 순위 : " + waitingCount + "\n\n";
						emitter.next(exchange.getResponse().bufferFactory().wrap(eventData.getBytes(StandardCharsets.UTF_8)));
					} else {
						retryCount.incrementAndGet();
						if (retryCount.get() >= maxRetries) {
							// 최대 재시도 횟수 초과 시 에러 처리
							String eventData = "data: 주문 상태를 확인할 수 없습니다. 재시도 실패.\n\n";
							emitter.next(exchange.getResponse().bufferFactory().wrap(eventData.getBytes(StandardCharsets.UTF_8)));
							emitter.complete();
							isCompleted.set(true);
							executor.shutdown();

							redisService.removeOrderStatus(memberId + "|" + productId);
						} else {
							// 재시도 메시지 전송
							String retryMessage = "data: 주문 상태를 확인 중, 재시도 " + retryCount.get() + "\n\n";
							emitter.next(exchange.getResponse().bufferFactory().wrap(retryMessage.getBytes(StandardCharsets.UTF_8)));
						}
					}
				}
			};

			executor.scheduleAtFixedRate(task, 0, 100, TimeUnit.MILLISECONDS);

			emitter.onDispose(() -> {
				executor.shutdown();
				redisService.removeZset(memberId);
			});
		});
	}

	private String getFinalOrderStatus(String memberId, String productId) {

		return redisService.getOrderStatus(memberId + productId);
	}

//	private Flux<Mono<DataBuffer>> createSseStream(String memberId, String productId, ServerWebExchange exchange) {
//		return Flux.interval(Duration.ofSeconds(1))
//				.map(sequence -> {
//					Long rank = redisService.getRank(memberId);
//					String eventData = "data: " + rank + "\n\n";
//
//					return exchange.getResponse().bufferFactory().wrap(eventData.getBytes(StandardCharsets.UTF_8));
//				})
//				.takeWhile(buffer -> redisService.getRank(memberId) > 0)
//				.concatWith(Flux.defer(() -> {
//					// 주문 처리 완료 후 최종 상태 전송
//					String finalStatus = getFinalOrderStatus(memberId, productId);
//					String eventData = "data: " + finalStatus + "\n\n";
//					DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(eventData.getBytes(StandardCharsets.UTF_8));
//					return Flux.just(buffer);
//				}))
//				.map(Mono::just)
//				.doFinally(signalType -> redisService.removeZset(memberId));
//	}

//	@Override
//	public GatewayFilter apply(GatewayOrderFilter.Config config) {
//
//		return (exchange, chain) -> {
//
//			ServerHttpRequest request = exchange.getRequest();
//
//			if (request.getPath().value().endsWith(PATH)) {
//
//				return exchange.getRequest().getBody().collectList().flatMap(dataBuffers -> {
//
//					StringBuilder bodyBuilder = new StringBuilder();
//
//					dataBuffers.forEach(buffer -> {
//						byte[] bytes = new byte[buffer.readableByteCount()];
//						buffer.read(bytes);
//						DataBufferUtils.release(buffer);
//						bodyBuilder.append(new String(bytes, StandardCharsets.UTF_8));
//					});
//
//					// JSON을 DTO로 변환
//					DiscountProductOrderRequestDTO orderRequest;
//					try {
//						orderRequest = objectMapper.readValue(bodyBuilder.toString(), DiscountProductOrderRequestDTO.class);
//					} catch (JsonProcessingException e) {
//						return Mono.error(new InternalServerErrorException("Invalid request body", e));
//					}
//
//					// 이벤트 발행 및 처리
//					queueEventProducer.sendQueueEvent(orderRequest);
//
//					return chain.filter(exchange);
//				});
//			}
//
//			return chain.filter(exchange);
//		};
//	}

	// Redis에 대기열 정보 저장
//					redisService.storeZset(orderRequest.memberId().toString());


	// 응답 헤더를 SSE 형식으로 설정
//					exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_EVENT_STREAM);

	// SSE를 위한 Flux 생성
//					Flux<ServerSentEvent<Long>> eventStream = Flux.interval(Duration.ofSeconds(1))
//							.map(sequence -> {
//
//								Long rank = redisService.getRank(orderRequest.memberId().toString());
//
//								return ServerSentEvent.<Long>builder()
//										.id(String.valueOf(sequence))
//										.event(EVENT)
//										.data(rank)
//										.build();
//							})
//							.takeUntil(event -> {
//								Long rank = redisService.getRank(orderRequest.memberId().toString());
//
//								return rank == 0;
//							})
//							.doFinally(signalType -> redisService.removeZset(orderRequest.memberId().toString()));

//					return exchange.getResponse().writeAndFlushWith(
//							eventStream.map(event -> {
//								Long data = event.data();
//								if (data != null) {
//									byte[] responseBytes = data.toString().getBytes();
//
//									return Flux.just(exchange.getResponse().bufferFactory().wrap(responseBytes));
//								}
//								return Flux.empty();
//							})
//					);

	public static class Config {
	}
}
