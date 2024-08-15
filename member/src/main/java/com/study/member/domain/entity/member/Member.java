package com.study.member.domain.entity.member;

import com.study.member.domain.common.BaseTimeEntity;
import com.study.member.domain.common.CommonConstant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Size(max = 50)
	@Column(unique = true)
	@Pattern(regexp = CommonConstant.RegExp.EMAIL)
	String email;

	@Size(max = 255)
	String password;

	String name;

	@Size(max = 50)
	@Column(unique = true)
	@Pattern(regexp = CommonConstant.RegExp.PHONE)
	String phone;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	MemberRole memberRole = MemberRole.ROLE_WAIT;

	@Builder.Default
	@ColumnDefault("true")
	Boolean isLocked = true;

}
