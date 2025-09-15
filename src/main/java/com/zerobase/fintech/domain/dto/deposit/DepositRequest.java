package com.zerobase.fintech.domain.dto.deposit;


import java.math.BigDecimal;
import java.util.Optional;
import lombok.Data;

@Data
public class DepositRequest {
  private String toAccountNumber;
  private BigDecimal amount;
}
