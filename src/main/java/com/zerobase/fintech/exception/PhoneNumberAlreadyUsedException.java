package com.zerobase.fintech.exception;

public class PhoneNumberAlreadyUsedException extends RuntimeException {

  public PhoneNumberAlreadyUsedException() {
    super("이미 등록된 전화번호입니다.");
  }
}
