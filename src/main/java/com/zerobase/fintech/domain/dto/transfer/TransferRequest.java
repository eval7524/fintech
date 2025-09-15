package com.zerobase.fintech.domain.dto.transfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferRequest {

  @NotBlank(message = "입금할 계좌번호 입력은 필수입니다.")
  private String toAccountNumber;

  @NotNull(message = "입금 금액 입력은 필수입니다.")
  @DecimalMin(value = "0.1", message = "입금 금액은 0보다 커야 합니다.")
  private BigDecimal amount;


}
