package com.zerobase.fintech.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.fintech.domain.dto.auth.SignUpRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SignUpRequestValidationTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validPassword_success() {
    SignUpRequest request = new SignUpRequest();
    request.setUsername("testUser");
    request.setPassword("Abcd1234!");
    request.setName("홍길동");
    request.setPhoneNumber("010-1234-5678");

    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);
    assertTrue(violations.isEmpty());
  }

  @Test
  void invalidPassword_fail() {
    SignUpRequest request = new SignUpRequest();
    request.setUsername("testUser");
    request.setPassword("abcd1234");
    request.setName("홍길동");
    request.setPhoneNumber("010-1234-5678");

    Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
  }

}
