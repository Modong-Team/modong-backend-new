package com.modong.backend.global.exception.application;

import com.modong.backend.global.exception.BadRequestException;

public class UrlIdDuplicateException extends BadRequestException {
  private static final String SERVER_MESSAGE = "이미 등록된 지원서 링크 입니다.";
  private static final String ERROR_CODE = "URL_ID_EXISTS";
  private static final String CLIENT_MESSAGE = "이미 등록된 지원서 링크 입니다.";

  public UrlIdDuplicateException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
