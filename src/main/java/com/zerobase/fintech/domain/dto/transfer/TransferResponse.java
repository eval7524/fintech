package com.zerobase.fintech.domain.dto.transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {

  private String fromAccountNumber;
  private String toAccountNumber;
  private BigDecimal amount;
  private BigDecimal balance;
  private LocalDateTime timestamp;

}
