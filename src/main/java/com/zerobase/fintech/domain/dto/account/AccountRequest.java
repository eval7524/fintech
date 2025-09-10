package com.zerobase.fintech.domain.dto.account;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AccountRequest {

  private String accountNumber;
  private Long memberId;
  private Long balance;
  private LocalDateTime createdAt;
  private String owner;
}
