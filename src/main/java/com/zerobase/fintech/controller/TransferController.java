package com.zerobase.fintech.controller;

import com.zerobase.fintech.domain.dto.transfer.TransferRequest;
import com.zerobase.fintech.domain.dto.transfer.TransferResponse;
import com.zerobase.fintech.service.transaction.TransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/accounts/{fromAccountId}/transfers")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Transaction API")

public class TransferController {

  private final TransferService transferService;

  @PostMapping
  public ResponseEntity<TransferResponse> transfer(
      @PathVariable Long fromAccountId,
      @Valid @RequestBody TransferRequest request) {
    TransferResponse response = transferService.transfer(fromAccountId, request);
    return ResponseEntity.ok(response);
  }

}
