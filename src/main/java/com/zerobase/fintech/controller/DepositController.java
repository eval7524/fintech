package com.zerobase.fintech.controller;

import com.zerobase.fintech.domain.dto.deposit.DepositRequest;
import com.zerobase.fintech.domain.dto.deposit.DepositResponse;
import com.zerobase.fintech.service.DepositService;
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
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class DepositController {

  private final DepositService depositService;

  @PostMapping()
  public ResponseEntity<DepositResponse> deposit(@RequestBody DepositRequest request) {
    DepositResponse response = depositService.deposit(request);
    return ResponseEntity.ok(response);
  }
}
