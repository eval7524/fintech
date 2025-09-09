package com.zerobase.fintech.exception;

public class InvalidPasswordException extends RuntimeException {

  public InvalidPasswordException() {
    super("비밀번호는 최소 8자리 이상이어야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
  }
}
