package com.modong.backend.global.exception;

public class IdNotFoundException extends NotFoundException {

  private static final String ERROR_CODE = "NOT_FOUND";
  private static final String SERVER_MESSAGE = "존재하지 않는 동아리 조회";
  private static final String CLIENT_MESSAGE = "동아리를 찾지 못했습니다.";

  public IdNotFoundException(String domain, final Long id) {
    super(String.format("%s -> %s id: %d", SERVER_MESSAGE, domain, id), CLIENT_MESSAGE, ERROR_CODE);
  }
}
