package com.zerobase.fintech.domain.repository;

import com.zerobase.fintech.domain.entity.Account;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByAccountNumber(String accountNumber);

  Page<Account> findAllByMemberId(Long memberId, Pageable pageable);

  Optional<Account> findByIdAndMemberId(Long accountId, Long memberId);

  Optional<Account> findByAccountNumber(String accountNumber);
}
