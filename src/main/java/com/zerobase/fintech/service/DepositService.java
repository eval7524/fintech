package com.zerobase.fintech.service;

import static com.zerobase.fintech.domain.entity.TransactionType.*;

import com.zerobase.fintech.domain.dto.deposit.DepositRequest;
import com.zerobase.fintech.domain.dto.deposit.DepositResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
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
   * 입금 대상 AccountNumber, 입금 금액 amount 계좌번호 유효성 검사
   * "무통장 입금"
   */


  //무통장 입금 기능 구현
  @Transactional
  public DepositResponse deposit(DepositRequest request) {
    Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
        .orElseThrow(() -> new AccountNotFoundException());
    log.info("입금 대상 계좌 조회 완료 toAccountNumber = {}", toAccount.getAccountNumber());
    toAccount.deposit(request.getAmount());
    log.info("입금 처리 완료 toAccountNumber = {}, amount = {}", toAccount.getAccountNumber(), request.getAmount());

    Transaction transaction = Transaction.builder()
        .toAccountId(toAccount)
        .amount(request.getAmount())
        .timestamp(LocalDateTime.now())
        .type(DEPOSIT)
        .build();

    transactionRepository.save(transaction);
    log.info("입금 거래 내역 저장 완료 계좌 번호 = {}, 금액 = {}, 일시 = {}", toAccount.getAccountNumber(),request.getAmount() ,transaction.getTimestamp());
    return new DepositResponse(request.getToAccountNumber(), request.getAmount(), transaction.getTimestamp());
  }
}
