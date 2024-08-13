package com.study.product.domain.entity;

import com.study.product.domain.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Builder
@NotBlank
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String name;

	Integer price;

	Integer stock;

	@Builder.Default
	@ColumnDefault("true")
	Boolean isVisible = true;

	public void modifyForProductField(String name, Integer price, Integer stock, Boolean isVisible) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.isVisible = isVisible;
	}
}
