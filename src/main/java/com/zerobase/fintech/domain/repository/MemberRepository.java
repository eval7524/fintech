package com.zerobase.fintech.domain.repository;

import com.zerobase.fintech.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  //Username = 로그인ID -> 중복 체크 필요
  Optional<Member> findByUsername(String username);
  //PhoneNumber = 휴대폰번호 -> 중복 체크 필요
  Optional<Member> findByPhoneNumber(String phoneNumber);

}
