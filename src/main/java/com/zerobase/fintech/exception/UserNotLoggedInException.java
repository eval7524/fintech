package com.zerobase.fintech.exception;

public class UserNotLoggedInException extends RuntimeException {

  public UserNotLoggedInException() {
    super("로그인이 필요합니다.");
  }
}
