package com.study.web.domain.entity;

import com.study.web.domain.common.BaseTimeEntity;
import com.study.web.domain.common.CommonConstant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member extends BaseTimeEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotBlank
	@Size(max = 50)
	@Column(unique = true)
	@Pattern(regexp = CommonConstant.RegExp.EMAIL)
	String email;

	@NotBlank
	@Size(max = 255)
	String password;

	@NotBlank
	String name;

	@NotBlank
	@Size(max = 50)
	@Column(unique = true)
	@Pattern(regexp = CommonConstant.RegExp.PHONE)
	String phone;

	@Builder.Default
	@ColumnDefault("0")
	Integer tokenVersion = 0;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	MemberRole memberRole = MemberRole.ROLE_WAIT;

	@Builder.Default
	@ColumnDefault("true")
	Boolean isLocked = true;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.getMemberRole().name()));
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return isLocked;
	}
}
