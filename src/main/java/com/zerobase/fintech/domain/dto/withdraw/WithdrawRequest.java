package com.zerobase.fintech.domain.dto.withdraw;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class WithdrawRequest {

  @NotBlank(message = "계좌번호 입력은 필수입니다.")
  private String AccountNumber;

  @NotNull(message = "출금 금액 입력은 필수입니다.")
  @DecimalMin(value = "0.1", message = "출금 금액은 0보다 커야 합니다.")
  private BigDecimal amount;


}
