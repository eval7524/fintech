package com.zerobase.fintech.domain.dto.deposit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositResponse {

  private String toAccountNumber;
  private BigDecimal amount;
  private LocalDateTime timestamp;

}
