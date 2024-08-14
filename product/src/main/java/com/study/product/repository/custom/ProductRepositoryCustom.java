package com.study.product.repository.custom;

import com.study.product.model.request.SearchConditionDTO;
import com.study.product.model.response.ProductResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

	List<ProductResponseDTO> search(Pageable pageable, SearchConditionDTO searchCondition);
}
