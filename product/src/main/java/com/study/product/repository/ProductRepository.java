package com.study.product.repository;

import com.study.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByIsVisibleTrue();
}
