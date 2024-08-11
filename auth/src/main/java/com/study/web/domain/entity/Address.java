package com.study.web.domain.entity;

import com.study.web.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address extends BaseTimeEntity {

	//todo: 우선 만들어두긴 했는데 써야되나 분리해야되나;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String name;

	@NotNull
	String address;

	@NotNull
	String zipCode;

	@NotNull
	String phone;

	@Builder.Default
	@ColumnDefault("false")
	Boolean isDefault = false;

	@ManyToOne
	@JoinColumn(name = "member_id")
	Member member;
}
