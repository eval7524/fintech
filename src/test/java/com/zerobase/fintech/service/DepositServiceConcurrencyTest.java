package com.zerobase.fintech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.zerobase.fintech.domain.dto.deposit.DepositRequest;
import com.zerobase.fintech.domain.dto.deposit.DepositResponse;
import com.zerobase.fintech.domain.entity.Account;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.TransactionRepository;
import com.zerobase.fintech.service.transaction.DepositService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class DepositServiceConcurrencyTest {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private DepositService depositService;

  private Account account;

  @BeforeEach
  void setUp() {
    account = Account.builder()
        .accountNumber("199-46016385-2")
        .balance(BigDecimal.valueOf(0.0))
        .createdAt(LocalDateTime.now())
        .build();

    accountRepository.save(account);
  }

  @Test
  public void 동시_입금_테스트() throws Exception {
      //given
    int threadCount = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    DepositRequest request = new DepositRequest();
    request.setToAccountNumber(account.getAccountNumber());
    request.setAmount(new BigDecimal("5000"));

      //when
    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          depositService.deposit(request);
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();

      //then
    Optional<Account> updated = accountRepository.findById(account.getId());
    System.out.println("최종 잔액: " + updated.get().getBalance());
    assertEquals(new BigDecimal("50000.00"), updated.get().getBalance());
  }


}
