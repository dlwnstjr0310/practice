package com.study.member.repository;

import com.study.member.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByMemberId(Long memberId);

}
