package com.modong.backend.global.exception.auth;

import com.modong.backend.global.exception.NotFoundException;

public class RefreshTokenNotFoundException extends NotFoundException{
  private static final String ERROR_CODE = "REFRESH_TOKEN_NOT_FOUND";
  private static final String SERVER_MESSAGE = "존재하지 않는 토큰 조회";
  private static final String CLIENT_MESSAGE = "해당 토큰 값을 찾지 못했습니다.";

  public RefreshTokenNotFoundException(final Long id) {
    super(String.format("%s -> member id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
  }
}
