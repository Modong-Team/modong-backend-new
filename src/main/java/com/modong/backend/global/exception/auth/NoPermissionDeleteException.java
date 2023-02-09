package com.modong.backend.global.exception.auth;

import com.modong.backend.global.exception.BadRequestException;

public class NoPermissionDeleteException extends NoPermissionException {

  private static final String SERVER_MESSAGE = "삭제할 권한이 없습니다!";
  private static final String ERROR_CODE = "NO_PERMISSION_TO_DELETE";
  private static final String CLIENT_MESSAGE = "삭제할 권한이 없습니다!";

  public NoPermissionDeleteException() {
    super(SERVER_MESSAGE, CLIENT_MESSAGE, ERROR_CODE);
  }
}
