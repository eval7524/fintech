package com.zerobase.fintech.service;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.zerobase.fintech.domain.dto.transfer.TransferRequest;
import com.zerobase.fintech.domain.dto.transfer.TransferResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.entity.Transaction;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import com.zerobase.fintech.exception.TransactionException.InsufficientBalanceException;
import com.zerobase.fintech.exception.TransactionException.UnAuthorizedException;
import com.zerobase.fintech.service.transaction.TransferService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

  @Mock
  private AuthService authService;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private TransferService transferService;

  private Member member;
  private Member toMember;
  private Account fromAccount;
  private Account toAccount;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .name("홍길동")
        .build();

    toMember = Member.builder()
        .id(2L)
        .name("김철수")
        .build();

    fromAccount = Account.builder()
        .id(1L)
        .member(member)
        .accountNumber("111-12345678-1")
        .balance(new BigDecimal("10000"))
        .build();

    toAccount = Account.builder()
        .id(2L)
        .member(toMember)
        .accountNumber("222-67742853-3")
        .balance(new BigDecimal("5000"))
        .build();
  }

  @Test
  void 송금_성공() {
    TransferRequest request = TransferRequest.builder()
        .toAccountNumber(toAccount.getAccountNumber())
        .amount(new BigDecimal("5000"))
        .build();

    when(authService.getCurrentMember()).thenReturn(member);
    when(accountRepository.findById(fromAccount.getId())).thenReturn(Optional.of(fromAccount));
    when(accountRepository.findByAccountNumber(toAccount.getAccountNumber())).thenReturn(Optional.of(toAccount));
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

    TransferResponse response = transferService.transfer(fromAccount.getId(), request);

    assertEquals(fromAccount.getAccountNumber(), response.getFromAccountNumber());
    assertEquals(toAccount.getAccountNumber(), response.getToAccountNumber());
    assertEquals(new BigDecimal("5000"), response.getAmount());
    assertEquals(new BigDecimal("5000"), fromAccount.getBalance());
    assertEquals(new BigDecimal("10000"), toAccount.getBalance());
  }

  @Test
  void 출금계좌_없음_예외() {
    TransferRequest request = TransferRequest.builder()
        .toAccountNumber(toAccount.getAccountNumber())
        .amount(new BigDecimal("5000"))
        .build();

    when(authService.getCurrentMember()).thenReturn(member);
    when(accountRepository.findById(fromAccount.getId())).thenReturn(Optional.empty());

    assertThrows(AccountNotFoundException.class, () -> transferService.transfer(fromAccount.getId(), request));
  }

  @Test
  void 입금계좌_없음_예외() {
    TransferRequest request = TransferRequest.builder()
        .toAccountNumber("333-00000000-0")
        .amount(new BigDecimal("5000"))
        .build();

    when(authService.getCurrentMember()).thenReturn(member);
    when(accountRepository.findById(fromAccount.getId())).thenReturn(Optional.of(fromAccount));
    when(accountRepository.findByAccountNumber("333-00000000-0")).thenReturn(Optional.empty());

    assertThrows(AccountNotFoundException.class, () -> transferService.transfer(fromAccount.getId(), request));
  }

  @Test
  void 출금계좌_주인아님_예외() {
    TransferRequest request = TransferRequest.builder()
        .toAccountNumber(toAccount.getAccountNumber())
        .amount(new BigDecimal("5000"))
        .build();

    Member otherMember = Member.builder()
        .id(3L)
        .name("이영희")
        .build();

    when(authService.getCurrentMember()).thenReturn(otherMember);
    when(accountRepository.findById(fromAccount.getId())).thenReturn(Optional.of(fromAccount));

    assertThrows(UnAuthorizedException.class, () -> transferService.transfer(fromAccount.getId(), request));
  }

  @Test
  void 잔액부족_예외() {
    TransferRequest request = TransferRequest.builder()
        .toAccountNumber(toAccount.getAccountNumber())
        .amount(new BigDecimal("15000"))
        .build();

    when(authService.getCurrentMember()).thenReturn(member);
    when(accountRepository.findById(fromAccount.getId())).thenReturn(Optional.of(fromAccount));
    when(accountRepository.findByAccountNumber(toAccount.getAccountNumber())).thenReturn(Optional.of(toAccount));

    assertThrows(InsufficientBalanceException.class, () -> transferService.transfer(fromAccount.getId(), request));
  }
}
