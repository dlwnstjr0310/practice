package com.study.member.domain.entity;

import com.study.member.domain.common.BaseTimeEntity;
import com.study.member.domain.common.CommonConstant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class Address extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Builder.Default
	@ColumnDefault("'기본 배송지'")
	String name = "기본 배송지";

	String address;

	String zipCode;

	@Pattern(regexp = CommonConstant.RegExp.PHONE)
	String phone;

	@Builder.Default
	@ColumnDefault("false")
	Boolean isDefault = false;

	
}
