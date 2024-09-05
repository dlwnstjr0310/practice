package com.study.order.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HikariCPMetricsLogger {

	private static final Logger logger = LoggerFactory.getLogger(HikariCPMetricsLogger.class);
	private final HikariDataSource hikariDataSource;

	@Scheduled(fixedRate = 1000)
	public void logHikariCPMetrics() {
		int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
		int idleConnections = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
		int totalConnections = hikariDataSource.getHikariPoolMXBean().getTotalConnections();
		int pendingConnections = hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();

		logger.info("HikariCP - Active: {}, Idle: {}, Total: {}, Pending: {}",
				activeConnections, idleConnections, totalConnections, pendingConnections);
	}

}
