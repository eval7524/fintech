package com.zerobase.fintech.exception;

public class TransactionException extends RuntimeException {

  public TransactionException(String message) {
    super(message);
  }

  public static class AccountNotFoundException extends TransactionException {

    public AccountNotFoundException() {
      super("계좌를 찾을 수 없습니다.");
    }
  }

  public static class InvalidAmountException extends TransactionException {

    public InvalidAmountException(String message) {
      super(message);
    }
  }
}
