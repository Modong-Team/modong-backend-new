package com.modong.backend.global.exception.auth;

public class NoPermissionException extends RuntimeException {
  private String errorCode = "FORBIDDEN";
  private String clientMessage = "권한이 없는 요청 입니다!";

  public NoPermissionException(final String serverMessage, final String clientMessage, final String errorCode) {
    super(serverMessage);
    this.clientMessage = clientMessage;
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getClientMessage() {
    return clientMessage;
  }

}
