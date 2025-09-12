package com.zerobase.fintech.controller;

import com.zerobase.fintech.domain.dto.account.AccountResponse;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.service.AccountService;
import java.util.List;
import javax.security.auth.login.AccountNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@lombok.RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  //계좌 생성
  @PostMapping
  public ResponseEntity<AccountResponse> createAccount() {
    AccountResponse response = accountService.createAccount();
    return ResponseEntity.ok(response);
  }

  //소유 계좌 전체 조회
  @GetMapping
  public ResponseEntity<List<AccountResponse>> getAccounts(Pageable pageable) {
    List<AccountResponse> accounts = accountService.findAllAccounts(pageable).getContent();
    return ResponseEntity.ok(accounts);
  }

  //소유 계좌 단건 조회
  @GetMapping("/{accountId}")
  public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) throws AccountNotFoundException {
    AccountResponse accountResponse = accountService.findAccountById(accountId);
    return ResponseEntity.ok(accountResponse);
  }
}
