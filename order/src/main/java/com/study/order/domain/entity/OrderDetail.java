package com.study.order.domain.entity;

import com.study.order.domain.common.BaseTimeEntity;
import com.study.order.domain.entity.order.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotNull
	Long productId;

	Integer quantity;

	Integer price;

	@Builder.Default
	@ColumnDefault("false")
	Boolean isDelete = false;

	@ManyToOne
	@JoinColumn(name = "order_id")
	Order order;

	public void markAsDelete() {
		this.isDelete = true;
	}
}
