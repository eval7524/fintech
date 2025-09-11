package com.zerobase.fintech.controller;

import com.zerobase.fintech.domain.dto.account.AccountResponse;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@lombok.RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping("/create")
  public ResponseEntity<AccountResponse> createAccount() {
    Member member = accountService.getCurrentMember();
    AccountResponse response = accountService.createAccount(member);
    return ResponseEntity.ok(response);
  }
}
