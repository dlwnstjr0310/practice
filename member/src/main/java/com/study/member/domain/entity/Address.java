package com.study.member.domain.entity;

import com.study.member.domain.common.BaseTimeEntity;
import com.study.member.domain.common.CommonConstant;
import com.study.member.domain.entity.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Builder.Default
	@ColumnDefault("'기본 배송지'")
	String name = "기본 배송지";

	@NotBlank
	String address;

	@NotBlank
	String zipCode;

	@NotBlank
	@Pattern(regexp = CommonConstant.RegExp.PHONE)
	String phone;

	@Builder.Default
	@ColumnDefault("false")
	Boolean isDefault = false;

	@ManyToOne
	@JoinColumn(name = "member_id")
	Member member;

	public void markAsFalse() {
		this.isDefault = false;
	}
}
