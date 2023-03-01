package com.modong.backend.global.exception;

public class ResourceNotFoundException extends BadRequestException {

  private static final String ERROR_CODE = "NOT_FOUND";
  private static final String SERVER_MESSAGE = "에 존재하지 않는 객체 조회";

  public ResourceNotFoundException(String domain, final Long id) {
    super(String.format("%s %s id: %d", domain, SERVER_MESSAGE, id), String.format("%s %s id: %d", domain, SERVER_MESSAGE, id), ERROR_CODE);
  }

  public ResourceNotFoundException(String domain,final String urlId) {
    super(String.format("%s %s url : %s", domain, SERVER_MESSAGE, urlId), String.format("%s %s url : %s", domain, SERVER_MESSAGE, urlId), ERROR_CODE);
  }

  public ResourceNotFoundException(String reason) {
    super(SERVER_MESSAGE, reason, ERROR_CODE);
  }

}
