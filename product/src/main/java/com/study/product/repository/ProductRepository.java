package com.study.product.repository;

import com.study.product.domain.entity.Product;
import com.study.product.repository.custom.ProductRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

	List<Product> findAllByIsVisibleTrue(Pageable pageable);
}
