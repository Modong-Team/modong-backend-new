package com.modong.backend.global.exception.auth;

public class NoPermissionReadException extends NoPermissionException {

  private static final String SERVER_MESSAGE = "조회 권한이 없습니다!";
  private static final String ERROR_CODE = "NO_PERMISSION_TO_CREATE";
  private static final String CLIENT_MESSAGE = "조회할 권한이 없습니다!";

  public NoPermissionReadException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
