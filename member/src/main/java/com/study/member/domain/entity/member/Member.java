package com.study.member.domain.entity.member;

import com.study.member.domain.common.BaseTimeEntity;
import com.study.member.domain.common.CommonConstant;
import com.study.member.domain.entity.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NotBlank
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

	@JoinColumn(name = "member_id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	List<Address> addressList;

}
