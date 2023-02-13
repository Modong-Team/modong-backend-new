package com.modong.backend.global.exception.evaluation;

import com.modong.backend.global.exception.BadRequestException;

public class AlreadyExistsException extends BadRequestException {
  private static final String SERVER_MESSAGE = "이미 %s가 있습니다.";
  private static final String ERROR_CODE = "ALREADY_EXISTS";
  private static final String CLIENT_MESSAGE = "이미 %s가 있습니다.";

  public AlreadyExistsException(String domain) {
    super(String.format(SERVER_MESSAGE, domain), ERROR_CODE, String.format(CLIENT_MESSAGE, domain));
  }
}
