package com.modong.backend.Enum;

public enum ErrorCode {
  // Common
  INVALID_INPUT_VALUE(400, "COMMON_001", " Invalid Input Value"),
  METHOD_NOT_ALLOWED(405, "COMMON_002", "Method not allowed"),
  HANDLE_ACCESS_DENIED(403, "COMMON_003", "Access is Denied"),

  // Standard
  ILLEGAL_STATE(400, "STANDARD_001", "illegal state"),
  ILLEGAL_ARGUMENT(400, "STANDARD_002", "illegal argument"),

  // Exception
  EXCEPTION(500, "EXCEPTION", "exception");

  private final String code;
  private final String message;
  private int status;

  ErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }

  public String getCode() { return code; }
  public String getMessage() { return message; }
  public int getStatus() { return status; }
}
