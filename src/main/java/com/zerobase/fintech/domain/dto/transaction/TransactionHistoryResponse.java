package com.zerobase.fintech.domain.dto.transaction;

import com.zerobase.fintech.domain.entity.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionHistoryResponse {
  private Long id;
  private String toAccountNumber;
  private BigDecimal amount;
  private String type;
  private LocalDateTime transactionDate;

  public static TransactionHistoryResponse fromEntity(Transaction transaction) {
    return new TransactionHistoryResponse(
        transaction.getId(),
        transaction.getToAccount().getAccountNumber(),
        transaction.getAmount(),
        String.valueOf(transaction.getType()),
        transaction.getTimestamp()
    );
  }

}
