package com.zerobase.fintech.service.transaction;

import static com.zerobase.fintech.domain.entity.TransactionType.*;

import com.zerobase.fintech.domain.dto.withdraw.WithdrawRequest;
import com.zerobase.fintech.domain.dto.withdraw.WithdrawResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import com.zerobase.fintech.exception.TransactionException.UnAuthorizedException;
import com.zerobase.fintech.service.AuthService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawService {

  private final AuthService authService;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  @Transactional
  public WithdrawResponse withdraw(WithdrawRequest request) {
    log.info("출금 시도 계좌번호 = {}, 금액 = {}", request.getAccountNumber(), request.getAmount());

    Member member = authService.getCurrentMember();
    Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
        .orElseThrow(() -> new AccountNotFoundException());

    if (!account.getMember().getId().equals(member.getId())) {
      log.error("본인 소유의 계좌가 아닙니다.");
      throw new UnAuthorizedException();
    }

    account.withdraw(request.getAmount());
    log.info("출금 처리 완료 계좌번호 = {}, 금액 = {}, 잔액 = {}", account.getAccountNumber(), request.getAmount(), account.getBalance());
    Transaction transaction = Transaction.builder()
        .fromAccount(account)
        .amount(request.getAmount())
        .type(WITHDRAW)
        .timestamp(LocalDateTime.now())
        .build();

    transactionRepository.save(transaction);
    log.info("출금 거래 내역 저장 완료 계좌 번호 = {}, 금액 = {}, 일시 = {}", account.getAccountNumber(), request.getAmount(), transaction.getTimestamp());
    return new WithdrawResponse(account.getAccountNumber(), request.getAmount(), account.getBalance(), transaction.getTimestamp());
  }

}
