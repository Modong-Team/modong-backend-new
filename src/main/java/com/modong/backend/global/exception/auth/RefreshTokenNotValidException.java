package com.modong.backend.global.exception.auth;

public class RefreshTokenNotValidException extends UnAuthorizedException {

  private static final String SERVER_MESSAGE = "유효하지 않은 토큰입니다.";
  private static final String ERROR_CODE = "REFRESH_TOKEN_IS_NOT_VALID";
  private static final String CLIENT_MESSAGE = "다시 로그인 해 주세요";

  public RefreshTokenNotValidException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
