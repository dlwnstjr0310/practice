package com.study.order.domain.entity.order;

import com.study.order.domain.common.BaseTimeEntity;
import com.study.order.domain.entity.OrderDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.study.order.domain.entity.order.Status.ORDER_COMPLETED;

@Entity
@Getter
@Setter
@Builder
@NotBlank
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	Long memberId;

	Integer totalPrice;

	String destinationAddress;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	Status status = ORDER_COMPLETED;

	@Builder.Default
	@ColumnDefault("false")
	Boolean isDelete = false;

	@JoinColumn(name = "order_id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	Set<OrderDetail> detailList = new LinkedHashSet<>();

}
