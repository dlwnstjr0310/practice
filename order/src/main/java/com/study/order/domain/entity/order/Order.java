package com.study.order.domain.entity.order;

import com.study.order.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import static com.study.order.domain.entity.order.Status.ORDER_PROGRESS;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotNull
	Long memberId;

	@NotNull
	Integer totalPrice;

	@NotNull
	String destinationAddress;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	Status status = ORDER_PROGRESS; // 상태 추가

	@Builder.Default
	@ColumnDefault("false")
	Boolean isDelete = false;

	public void updateStatus(Status status) {
		this.status = status;
	}

	public void updateTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}
}
