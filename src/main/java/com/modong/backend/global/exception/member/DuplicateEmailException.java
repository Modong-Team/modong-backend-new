package com.modong.backend.global.exception.member;

import com.modong.backend.global.exception.BadRequestException;

public class DuplicateEmailException extends BadRequestException {
  private static final String SERVER_MESSAGE = "이미 등록된 이메일 입니다.";
  private static final String ERROR_CODE = "EMAIL_EXISTS";
  private static final String CLIENT_MESSAGE = "이미 등록된 아이디 입니다.";

  public DuplicateEmailException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
