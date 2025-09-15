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

    public InvalidAmountException() {
      super("출금 금액은 0보다 크거나, Null이 아니어야 합니다.");
    }
  }

  public static class UnAuthorizedException extends TransactionException {

    public UnAuthorizedException() {
      super("권한이 없습니다.");
    }
  }

  public static class InsufficientBalanceException extends TransactionException {

    public InsufficientBalanceException() {
      super("잔액이 부족합니다.");
    }
  }
}
