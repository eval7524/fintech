package com.zerobase.fintech.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.fintech.domain.dto.withdraw.WithdrawRequest;
import com.zerobase.fintech.domain.dto.withdraw.WithdrawResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import com.zerobase.fintech.exception.TransactionException.InsufficientBalanceException;
import com.zerobase.fintech.exception.TransactionException.UnAuthorizedException;
import com.zerobase.fintech.service.transaction.WithdrawService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WithdrawServiceTest {

  @Mock
  private AuthService authService;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private WithdrawService withdrawService;

  private Member member;
  private Account account;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .name("홍길동")
        .build();

    account = Account.builder()
        .id(1L)
        .accountNumber("199-46016385-2")
        .balance(java.math.BigDecimal.valueOf(10000))
        .member(member)
        .build();
  }

  @Test
  void 출금_성공() {
    //given
    WithdrawRequest request = new WithdrawRequest();
    request.setAccountNumber(account.getAccountNumber());
    request.setAmount(new BigDecimal("5000"));

    when(authService.getCurrentMember()).thenReturn(member);
    when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

    //when
    WithdrawResponse response = withdrawService.withdraw(request);

    //then
    assertEquals(account.getAccountNumber(), response.getAccountNumber());
    assertEquals(request.getAmount(), response.getAmount());
    assertEquals(BigDecimal.valueOf(5000), response.getBalance());
    verify(transactionRepository).save(any(Transaction.class));
  }

  @Test
  void 출금실패_계좌없음() {
    //given
    WithdrawRequest request = new WithdrawRequest();
    request.setAccountNumber("000-00000000-0");
    request.setAmount(new BigDecimal("5000"));

    when(authService.getCurrentMember()).thenReturn(member);
    when(accountRepository.findByAccountNumber("000-00000000-0")).thenReturn(Optional.empty());

    //when & then
    assertThrows(AccountNotFoundException.class, () -> withdrawService.withdraw(request));
  }

  @Test
  void 출금실패_본인계좌아님() {
    //given
    WithdrawRequest request = new WithdrawRequest();
    request.setAccountNumber(account.getAccountNumber());

    Member otherMember = Member.builder()
        .id(2L)
        .name("김철수")
        .build();

    when(authService.getCurrentMember()).thenReturn(otherMember);
    when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
    //when & then
    assertThrows(UnAuthorizedException.class, () -> withdrawService.withdraw(request));
  }

  @Test
  void 출금실패_잔액부족() {
    //given
    WithdrawRequest request = new WithdrawRequest();
    request.setAccountNumber(account.getAccountNumber());
    request.setAmount(new BigDecimal("15000"));

    when(authService.getCurrentMember()).thenReturn(member);
    when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

    //when & then
    assertThrows(InsufficientBalanceException.class, () -> withdrawService.withdraw(request));
  }
}


