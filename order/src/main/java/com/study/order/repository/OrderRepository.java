package com.study.order.repository;

import com.study.order.domain.entity.order.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Override
	@EntityGraph(attributePaths = "orderDetailList")
	Optional<Order> findById(Long id);
}
