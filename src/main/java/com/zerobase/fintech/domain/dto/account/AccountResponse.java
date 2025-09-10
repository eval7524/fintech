package com.zerobase.fintech.domain.dto.account;


import java.time.LocalDateTime;
import lombok.Data;

/**
 * 응답Dto -> 계좌 번호, 잔액, 생성일, 소유자
 */
@Data
public class AccountResponse {

  private String accountNumber;
  private Long balance;
  private LocalDateTime createdAt;
  private String owner;


}
