package com.zerobase.fintech.controller;

import com.zerobase.fintech.domain.dto.withdraw.WithdrawRequest;
import com.zerobase.fintech.domain.dto.withdraw.WithdrawResponse;
import com.zerobase.fintech.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/accounts/{accountId}/withdraws")
@RequiredArgsConstructor
public class WithdrawController {

  private final WithdrawService withdrawService;

  @PostMapping
  public ResponseEntity<WithdrawResponse> withdraw(
      @PathVariable Long accountId,
      @RequestBody WithdrawRequest request) {
    WithdrawResponse response = withdrawService.withdraw(request);
    return ResponseEntity.ok(response);
  }

}
