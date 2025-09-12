package com.zerobase.fintech.service;

import com.zerobase.fintech.domain.dto.deposit.DepositRequest;
import com.zerobase.fintech.domain.dto.deposit.DepositResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.entity.TransactionType;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.exception.AccountNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;


  /**
   * 입금 대상 AccountNumber, 입금 금액 amount 계좌번호 유효성 검사 (실재하는지)
   */


  //입금 기능 구현
  @Transactional
  public DepositResponse deposit(DepositRequest request) {
    Account fromAccount = null;
    if (request.getFromAccountNumber() != null && !request.getFromAccountNumber().isEmpty()) {
      fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
          .orElseThrow(() -> new AccountNotFoundException());
    }
    Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
        .orElseThrow(() -> new AccountNotFoundException());


    toAccount.deposit(request.getAmount());
    accountRepository.save(toAccount);

    Transaction.TransactionBuilder transactionBuilder = Transaction.builder()
        .toAccountId(toAccount)
        .amount(request.getAmount())
        .type(TransactionType.DEPOSIT)
        .timestamp(LocalDateTime.now());

    if(fromAccount != null) {
      transactionBuilder.fromAccountId(fromAccount);
    }

    Transaction transaction = transactionBuilder.build();
    transactionRepository.save(transaction);


    DepositResponse depositResponse = new DepositResponse();
    depositResponse.setToAccountNumber(toAccount.getAccountNumber());
    depositResponse.setAmount(request.getAmount());
    depositResponse.setTimestamp(transaction.getTimestamp());

    return depositResponse;
  }
}
