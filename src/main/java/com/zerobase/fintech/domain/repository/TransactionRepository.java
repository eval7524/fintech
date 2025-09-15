package com.zerobase.fintech.domain.repository;

import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.entity.TransactionType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Page<Transaction> findAllByToAccountAndTimestampBetween(
      Account toAccount,
      LocalDateTime fromDate,
      LocalDateTime toDate,
      Pageable pageable
  );


  Page<Transaction> findAllByToAccountAndTimestampBetweenAndType(
      Account toAccount,
      LocalDateTime fromDate,
      LocalDateTime toDate,
      TransactionType type,
      Pageable pageable);
}
