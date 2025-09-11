package com.zerobase.fintech.service;

import com.zerobase.fintech.domain.dto.account.AccountResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.exception.UserNotLoggedInException;
import com.zerobase.fintech.security.CustomUserDetails;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final AuthService authService;

  //로그인 유무 확인
  public Member getCurrentMember() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      throw new UserNotLoggedInException();
    }
    Object principal = authentication.getPrincipal();
    if (!(principal instanceof CustomUserDetails)) {
      throw new UserNotLoggedInException();
    }
    CustomUserDetails customUserDetails = (CustomUserDetails) principal;
    return customUserDetails.getMember();
  }
  //계좌 생성
  @Transactional
  public AccountResponse createAccount(Member member) {
    log.info("계좌 생성 시도");

    //계좌번호 생성
    String accountNumber;
    do {
      accountNumber = generateAccountNumber();
    } while (accountRepository.existsByAccountNumber(accountNumber));

    Account account = Account.builder()
        .accountNumber(accountNumber)
        .balance(BigDecimal.valueOf(0.00))
        .member(member)
        .createdAt(LocalDateTime.now())
        .build();

    Account savedAccount = accountRepository.save(account);

    log.info("계좌 생성 완료 accountNumber = {}, balance = {}", savedAccount.getAccountNumber(), savedAccount.getBalance());
    return new AccountResponse(savedAccount.getAccountNumber(), savedAccount.getBalance(), savedAccount.getCreatedAt(),
        savedAccount.getMember().getName(), savedAccount.getMember().getId());
  }

  private String generateAccountNumber() {
    String bankCode = String.format("%03d", ThreadLocalRandom.current().nextInt(1, 1000));
    String body = String.format("%08d", ThreadLocalRandom.current().nextInt(10000000, 100000000));

    String combined = bankCode + body;
    int checkSum = combined.chars().map(Character::getNumericValue).sum() % 10;

    return String.format("%s-%s-%d", bankCode, body, checkSum);
  }
}
