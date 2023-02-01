package com.modong.backend.global.exception;

public class StatusBadRequestException extends BadRequestException{

  private static final String SERVER_MESSAGE = "이미 요청과 동일한 상태입니다. ";
  private static final String ERROR_CODE = "ALREADY_IN_THE_SAME_STATE";
  private static final String CLIENT_MESSAGE = "이미 요청과 동일한 상태입니다. ";

  public StatusBadRequestException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
