package com.study.member.repository;

import com.study.member.domain.entity.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Override
	@EntityGraph(attributePaths = "addressList")
	Optional<Member> findById(Long id);
}
