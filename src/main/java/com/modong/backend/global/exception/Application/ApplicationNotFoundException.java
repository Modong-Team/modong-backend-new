package com.modong.backend.global.exception.Application;

import com.modong.backend.global.exception.NotFoundException;

public class ApplicationNotFoundException extends NotFoundException {

  private static final String ERROR_CODE = "APPLICATION_NOT_FOUND";
  private static final String SERVER_MESSAGE = "존재하지 않는 지원서 조회";
  private static final String CLIENT_MESSAGE = "지원서를 찾지 못했습니다.";

  public ApplicationNotFoundException(final Long id) {
    super(String.format("%s -> application id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
  }
  public ApplicationNotFoundException(final String urlId) {
    super(String.format("%s -> application urlId: %s", SERVER_MESSAGE, urlId), CLIENT_MESSAGE, ERROR_CODE);
  }
}
