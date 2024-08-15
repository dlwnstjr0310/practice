package com.study.member.domain.entity;

import com.study.member.domain.entity.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
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

	@NotNull
	Integer price;

	@ManyToOne
	@JoinColumn(name = "member_id")
	Member member;

	public void modifyForWishListQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
