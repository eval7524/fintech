package com.zerobase.fintech.domain.repository;

import com.zerobase.fintech.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
