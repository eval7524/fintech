package com.zerobase.fintech.domain.repository;

import com.zerobase.fintech.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
