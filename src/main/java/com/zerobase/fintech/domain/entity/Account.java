package com.zerobase.fintech.domain.entity;


import com.zerobase.fintech.exception.InvalidAmountException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Column(nullable = false, unique = true, length = 15)
  private String accountNumber;

  @Column(nullable = false)
  private BigDecimal balance = BigDecimal.valueOf(0.00);


  //계좌 생성일
  @Column(nullable = false)
  private LocalDateTime createdAt;

  //입금
  public void deposit(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.valueOf(0.0)) <= 0) {
      throw new InvalidAmountException("입금 금액은 0보다 크거나, Null이 아니어야 합니다.");
    }
    this.balance = this.balance.add(amount);
  }

  //출금
  public void withdraw(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.valueOf(0.0)) <= 0) {
      throw new InvalidAmountException("출금 금액은 0보다 크거나, Null이 아니어야 합니다.");
    } else if (this.balance.compareTo(amount) < 0) {
      throw new InvalidAmountException("잔액이 부족합니다.");
    }
    this.balance = this.balance.subtract(amount);
  }

}
