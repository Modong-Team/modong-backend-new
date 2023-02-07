package com.modong.backend.global.exception;

public class IsClosed extends BadRequestException {

  private static final String SERVER_MESSAGE = "이미 마감된 지원서 입니다.";
  private static final String ERROR_CODE = "APPLICATION_ALREADY_IN_CLOSED";
  private static final String CLIENT_MESSAGE = "이미 마감된 지원서 입니다.";

  public IsClosed() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
