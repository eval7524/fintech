package com.zerobase.fintech.controller;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.*;

import com.zerobase.fintech.domain.dto.transaction.TransactionHistoryResponse;
import com.zerobase.fintech.domain.entity.TransactionType;
import com.zerobase.fintech.service.transaction.TransactionHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Transaction API")
public class TransactionController {

  private final TransactionHistoryService transactionHistoryService;

  @GetMapping
  ResponseEntity<Page<TransactionHistoryResponse>> transactionHistoriesById(
      @PathVariable Long accountId,
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate toDate,
      Pageable pageable) {

    Page<TransactionHistoryResponse> transactions = transactionHistoryService.getTransactionHistoriesById(accountId,
        fromDate, toDate, pageable);
    return ResponseEntity.ok(transactions);
  }

  @GetMapping("/type")
  ResponseEntity<Page<TransactionHistoryResponse>> transactionHistoriesByType(
      @PathVariable Long accountId,
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate fromDate,
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate toDate,
      @RequestParam(required = false) TransactionType type,
      Pageable pageable) {

    Page<TransactionHistoryResponse> transactions = transactionHistoryService.getTransactionHistoriesByType(
        accountId, fromDate, toDate, type, pageable);
    return ResponseEntity.ok(transactions);
  }
}
