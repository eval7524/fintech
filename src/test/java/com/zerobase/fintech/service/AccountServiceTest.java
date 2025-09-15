package com.zerobase.fintech.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.zerobase.fintech.domain.dto.account.AccountResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  private Member createMember() {
    return Member.builder()
        .id(1L)
        .username("testUser")
        .name("testName")
        .password("encodedPassword")
        .build();

  }

  private Account createAccount(Member member) {
    return Account.builder()
        .id(1L)
        .member(member)
        .accountNumber("123-45678901-2")
        .balance(BigDecimal.valueOf(0.0))
        .createdAt(java.time.LocalDateTime.now())
        .build();
  }



  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AuthService authService;

  @InjectMocks
  private AccountService accountService;

  @Test
  void 계좌생성_테스트() {
    Member member = createMember();
    given(authService.getCurrentMember()).willReturn(member);


    Mockito.when(accountRepository.existsByAccountNumber(Mockito.anyString())).thenReturn(false);
    Mockito.when(accountRepository.save(Mockito.any(Account.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    AccountResponse response = accountService.createAccount();

    assertNotNull(response.getAccountNumber());
    assertEquals(BigDecimal.valueOf(0.0), response.getBalance());
    assertEquals(member.getId(), response.getMemberId());
  }

  @Test
  void 계좌전체조회_성공() {
    //BDD 스타일 써보기
    //given
    Member member = createMember();
    Account account = createAccount(member);
    PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

    given(authService.getCurrentMember()).willReturn(member);
    given(accountRepository.findAllByMemberId(member.getId(), pageable))
        .willReturn(new PageImpl<>(List.of(account)));

    //when
    Page<AccountResponse> result = accountService.findAllAccounts(pageable);

    //then
    assertThat(result.getTotalElements()).isEqualTo(1);
    assertThat(result.getContent().get(0).getAccountNumber()).isEqualTo(account.getAccountNumber());
  }

  @Test
  void 계좌단건조회_성공() {
    //given
    Member member = createMember();
    Account account = createAccount(member);

    given(authService.getCurrentMember()).willReturn(member);
    given(accountRepository.findByIdAndMemberId(account.getId(), member.getId()))
        .willReturn(Optional.of(account));

    //when
    AccountResponse result = accountService.findAccountById(account.getId());

    //then
    assertThat(result.getAccountNumber()).isEqualTo(account.getAccountNumber());
  }

  @Test
  void 계좌단건조회_실패() {
    //given
    Member member = createMember();

    given(authService.getCurrentMember()).willReturn(member);
    given(accountRepository.findByIdAndMemberId(anyLong(), eq(member.getId())))
        .willReturn(Optional.empty());

    //when & then
    assertThrows(AccountNotFoundException.class, () -> {
      accountService.findAccountById(100L);
    });
  }

}