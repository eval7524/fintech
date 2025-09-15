package com.zerobase.fintech.domain.dto.withdraw;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawResponse {

  private String accountNumber;
  private BigDecimal amount;
  private BigDecimal balance;
  private LocalDateTime timestamp;

}
