package com.modong.backend.global.exception;

public class WrongFormatException extends BadRequestException{

  private static final String SERVER_MESSAGE = "데이터 형식이 잘못됐습니다.";
  private static final String ERROR_CODE = "FORMAT_IS_WRONG";
  private static final String CLIENT_MESSAGE = "데이터 형식을 확인해 주세요.";

  public WrongFormatException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
