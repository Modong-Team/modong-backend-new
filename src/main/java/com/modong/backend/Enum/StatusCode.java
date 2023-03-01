package com.modong.backend.Enum;

public enum StatusCode {
  BEFORE_OPENING(false),OPEN(false), CLOSE(true);

  boolean closed;

  StatusCode(boolean closed) {
    this.closed = closed;
  }
}
