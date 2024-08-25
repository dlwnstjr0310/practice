package com.study.member.domain.entity;

import com.study.member.domain.entity.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotNull
	Long productId;

	@NotNull
	Integer quantity;

	@ManyToOne
	@JoinColumn(name = "member_id")
	Member member;

	public void updateForQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
