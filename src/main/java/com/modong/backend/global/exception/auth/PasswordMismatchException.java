package com.modong.backend.global.exception.auth;


import com.modong.backend.global.exception.BadRequestException;

public class PasswordMismatchException extends BadRequestException {
  private static final String SERVER_MESSAGE = "비밀번호를 잘못입력했습니다.";
  private static final String ERROR_CODE = "PASSWORD_MISMATCH";
  private static final String CLIENT_MESSAGE = "비밀번호를 확인해주세요.";

  public PasswordMismatchException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
