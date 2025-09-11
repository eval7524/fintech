package com.zerobase.fintech.domain.dto.account;


import com.zerobase.fintech.domain.entity.Account;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 응답Dto -> 계좌 번호, 잔액, 생성일, 소유자
 */
@Data
@Getter
@AllArgsConstructor
public class AccountResponse {

  private String accountNumber;
  private BigDecimal balance;
  private LocalDateTime createdAt;
  private String owner;
  private Long memberId;

  public static AccountResponse fromEntity(Account account) {
    return new AccountResponse(
        account.getAccountNumber(),
        account.getBalance(),
        account.getCreatedAt(),
        account.getMember().getUsername(),
        account.getMember().getId()
    );
  }
}
