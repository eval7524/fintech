package com.zerobase.fintech.service;


import static com.zerobase.fintech.domain.entity.TransactionType.*;

import com.zerobase.fintech.domain.dto.transfer.TransferRequest;
import com.zerobase.fintech.domain.dto.transfer.TransferResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import com.zerobase.fintech.exception.TransactionException.UnAuthorizedException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

  private final AuthService authService;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  @Transactional
  public TransferResponse transfer(Long fromAccountId, TransferRequest request) {
    Member member = authService.getCurrentMember();
    Account fromAccount = accountRepository.findById(fromAccountId)
        .orElseThrow(() -> new AccountNotFoundException());

    log.info("이체 시도 출금 계좌 : {}, 입금 계좌 : {}, 금액 : {}", fromAccount.getAccountNumber(), request.getToAccountNumber(), request.getAmount());

    if (!fromAccount.getMember().getId().equals(member.getId())) {
      log.error("본인 소유의 계좌가 아닙니다.");
      throw new UnAuthorizedException();
    }

    Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
        .orElseThrow(() -> new AccountNotFoundException());
    log.info("입출금 계좌 조회 완료 : 출금 계좌 = {}, 입금 계좌 = {}", fromAccount.getAccountNumber(), toAccount.getAccountNumber());

    log.info("이체 시도 : 출금 계좌 = {}, 입금 계좌 = {}, 금액 = {}", fromAccount.getAccountNumber(), toAccount.getAccountNumber(), request.getAmount());
    fromAccount.withdraw(request.getAmount());
    toAccount.deposit(request.getAmount());
    log.info("이체 완료 : 출금 계좌 = {}, 입금 계좌 = {}, 금액 = {}, 출금 계좌 잔액 = {}", fromAccount.getAccountNumber(), toAccount.getAccountNumber(), request.getAmount(), fromAccount.getBalance());

    Transaction transaction = Transaction.builder()
        .fromAccountId(fromAccount)
        .toAccountId(toAccount)
        .amount(request.getAmount())
        .type(TRANSFER)
        .timestamp(LocalDateTime.now())
        .build();


    transactionRepository.save(transaction);
    log.info("이체 거래 내역 저장 완료 : 출금 계좌 = {}, 입금 계좌 = {}, 금액 = {}, 일시 = {}", fromAccount.getAccountNumber(), toAccount.getAccountNumber(), request.getAmount(), transaction.getTimestamp());

    TransferResponse transferResponse = TransferResponse.builder()
        .fromAccountNumber(fromAccount.getAccountNumber())
        .toAccountNumber(toAccount.getAccountNumber())
        .amount(request.getAmount())
        .balance(fromAccount.getBalance())
        .timestamp(transaction.getTimestamp())
        .build();

    return transferResponse;

  }

}
