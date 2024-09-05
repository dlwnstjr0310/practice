package com.study.order.repository;

import com.study.order.domain.entity.OrderDetail;
import com.study.order.repository.custom.OrderDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, OrderDetailRepositoryCustom {

	List<OrderDetail> findAllByOrderId(Long id);

}
