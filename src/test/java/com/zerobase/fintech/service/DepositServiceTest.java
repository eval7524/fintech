package com.zerobase.fintech.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.fintech.domain.dto.deposit.DepositRequest;
import com.zerobase.fintech.domain.dto.deposit.DepositResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import com.zerobase.fintech.exception.TransactionException.InvalidAmountException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepositServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private DepositService depositService;

  private Account account;

  @BeforeEach
  void setUp() {
    account = Account.builder()
        .accountNumber("199-46016385-2")
        .balance(BigDecimal.valueOf(0.0))
        .build();
  }

  @Test
  @DisplayName("존재하지 않는 계좌로 입금 시 예외 발생")
  void 무통장입금_계좌없음() {
      // Given
      DepositRequest request = new DepositRequest();
      request.setToAccountNumber("000-00000000-0");
      request.setAmount(new BigDecimal("5000"));

      when(accountRepository.findByAccountNumber("000-00000000-0"))
          .thenReturn(java.util.Optional.empty());

      // When & Then
      assertThrows(AccountNotFoundException.class, () -> depositService.deposit(request));

      verify(accountRepository, times(1)).findByAccountNumber("000-00000000-0");
      verify(transactionRepository, never()).save(any());
  }

  @Test
  @DisplayName("0 이하 금액 입금 시 예외 발생")
  void 무통장입금_금액오류_실패() {
      // Given
      DepositRequest request = new DepositRequest();
      request.setToAccountNumber("199-46016385-2");
      request.setAmount(new BigDecimal("0"));

      when(accountRepository.findByAccountNumber("199-46016385-2"))
          .thenReturn(java.util.Optional.of(account));

      // When & Then
      assertThrows(InvalidAmountException.class, () -> depositService.deposit(request));

      verify(accountRepository, times(1)).findByAccountNumber("199-46016385-2");
      verify(transactionRepository, never()).save(any());
  }

  @Test
  @DisplayName("무통장 입금 테스트 성공")
  void 무통장입금_성공() {
    // Given
    DepositRequest request = new DepositRequest();
    request.setToAccountNumber("199-46016385-2");
    request.setAmount(new BigDecimal("5000"));

    when(accountRepository.findByAccountNumber("199-46016385-2"))
        .thenReturn(java.util.Optional.of(account));
    when(transactionRepository.save(any(Transaction.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    // When
    DepositResponse response = depositService.deposit(request);

    // Then
    assertEquals("199-46016385-2", response.getToAccountNumber());
    assertEquals(0,request.getAmount().compareTo(response.getAmount()));
    assertNotNull(response.getTimestamp());
    assertEquals(0, request.getAmount().compareTo(account.getBalance()));

    verify(accountRepository, times(1)).findByAccountNumber("199-46016385-2");
    verify(transactionRepository, times(1)).save(any(Transaction.class));
  }

}
