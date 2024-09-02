package com.study.order.config;

import com.study.order.exception.OrderException;
import com.study.order.exception.order.OrderBeenCanceledException;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class ThreadConfig implements AsyncConfigurer {

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8); // I/O 바운드라면 코어 수의 2배로 설정 가능
		executor.setMaxPoolSize(16); // 최대 스레드 수
		executor.setQueueCapacity(100000); // 대기열 크기
		executor.setThreadNamePrefix("OrderTask-"); // 스레드 이름 접두사
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, objects) -> {

			if (ex instanceof OrderException) {
				throw new OrderBeenCanceledException();
			} else {
				ex.printStackTrace();
			}
		};
	}


}
