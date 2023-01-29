package com.modong.backend.global.exception.member;

import com.modong.backend.global.exception.BadRequestException;

public class DuplicatePhoneException extends BadRequestException {
  private static final String SERVER_MESSAGE = "이미 존재하는 휴대폰번호 입니다.";
  private static final String ERROR_CODE = "PHONE_EXISTS";
  private static final String CLIENT_MESSAGE = "이미 존재하는 휴대폰번호 입니다.!";

  public DuplicatePhoneException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}

