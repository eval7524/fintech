package com.zerobase.fintech.service;

import com.zerobase.fintech.domain.dto.account.AccountResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final AuthService authService;


  //계좌 생성
  @Transactional
  public AccountResponse createAccount() {
    Member member = authService.getCurrentMember();
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

  //계좌 번호 생성
  private String generateAccountNumber() {
    String bankCode = String.format("%03d", ThreadLocalRandom.current().nextInt(1, 1000));
    String body = String.format("%08d", ThreadLocalRandom.current().nextInt(10000000, 100000000));

    String combined = bankCode + body;
    int checkSum = combined.chars().map(Character::getNumericValue).sum() % 10;

    return String.format("%s-%s-%d", bankCode, body, checkSum);
  }

  //계좌 조회, 페이징 처리
  public Page<AccountResponse> findAllAccounts(Pageable pageable) {
    Member member = authService.getCurrentMember();
    Pageable sortedPageable = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(Direction.DESC, "createdAt")
    );
    log.info("계좌 전체 조회 : username = {}", authService.getCurrentMember().getUsername());
    return accountRepository.findAllByMemberId(member.getId(), sortedPageable).map(AccountResponse::fromEntity);
  }

  public AccountResponse findAccountById(Long accountId) {
    Member member = authService.getCurrentMember();
    Account account = accountRepository.findByIdAndMemberId(accountId, member.getId())
        .orElseThrow(() -> new AccountNotFoundException());
    log.info("계좌 단건 조회 : accountId = {}, username = {}", accountId, authService.getCurrentMember().getUsername());
    return AccountResponse.fromEntity(account);
  }
}
