package com.modong.backend.global.exception.auth;

public class NoPermissionUpdateException extends NoPermissionException {

  private static final String SERVER_MESSAGE = "수정할 권한이 없습니다!";
  private static final String ERROR_CODE = "NO_PERMISSION_TO_CREATE";
  private static final String CLIENT_MESSAGE = "수정할 권한이 없습니다!";

  public NoPermissionUpdateException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}