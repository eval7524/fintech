package com.zerobase.fintech.domain.repository;

import com.zerobase.fintech.domain.entity.Account;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByAccountNumber(String accountNumber);

  Page<Account> findAllByMemberId(Long memberId, Pageable pageable);

  Optional<Account> findByIdAndMemberId(Long accountId, Long memberId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Account> findByAccountNumber(String accountNumber);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Account> findById(Long accountId);
}
