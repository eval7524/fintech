package com.zerobase.fintech.service.transaction;


import com.zerobase.fintech.domain.dto.transaction.TransactionHistoryResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.entity.TransactionType;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionHistoryService {

  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  public Page<TransactionHistoryResponse> getTransactionHistoriesById(
      Long accountId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException());

    Pageable sortedPageable = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(Sort.Direction.DESC, "timestamp")  // 최신순
    );

    LocalDateTime start = fromDate.atStartOfDay();
    LocalDateTime end = toDate.plusDays(1).atStartOfDay().minusNanos(1);

    Page<TransactionHistoryResponse> transactions = transactionRepository.findAllByToAccountAndTimestampBetween(account, start, end,
        sortedPageable).map(TransactionHistoryResponse::fromEntity);

    return transactions;
  }

  public Page<TransactionHistoryResponse> getTransactionHistoriesByType(
      Long accountId, LocalDate fromDate, LocalDate toDate, TransactionType type, Pageable pageable) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException());

    Pageable sortedPageable = PageRequest.of(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        Sort.by(Sort.Direction.DESC, "timestamp")  // 최신순
    );

    LocalDateTime start = fromDate.atStartOfDay();
    LocalDateTime end = toDate.plusDays(1).atStartOfDay().minusNanos(1);

    Page<TransactionHistoryResponse> transactions = transactionRepository.findAllByToAccountAndTimestampBetweenAndType(account, start, end, type,
        sortedPageable).map(TransactionHistoryResponse::fromEntity);

    return transactions;

  }
}
