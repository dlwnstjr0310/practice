package com.study.web.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

	ROLE_WAIT("회원가입 승인 요청중", "0"),
	ROLE_BUYER("구매자", "1"),
	ROLE_SELLER("판매자", "2");

	private final String description;
	private final String profile;

}
