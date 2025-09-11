package com.zerobase.fintech.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.fintech.domain.dto.account.AccountResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.repository.AccountRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountService accountService;

  @Test
  void createAccountTest() {
    Member member = Member.builder()
        .id(1L)
        .username("홍길동")
        .password("encodedPassword")
        .build();

    Mockito.when(accountRepository.existsByAccountNumber(Mockito.anyString())).thenReturn(false);
    Mockito.when(accountRepository.save(Mockito.any(Account.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    AccountResponse response = accountService.createAccount(member);

    assertNotNull(response.getAccountNumber());
    assertEquals(BigDecimal.valueOf(0.0), response.getBalance());
    assertEquals(member.getId(), response.getMemberId());
  }

}