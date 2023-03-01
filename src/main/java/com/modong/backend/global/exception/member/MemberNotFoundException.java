package com.modong.backend.global.exception.member;

import com.modong.backend.global.exception.BadRequestException;

public class MemberNotFoundException extends BadRequestException {
  private static final String ERROR_CODE = "MEMBER_NOT_FOUND";
  private static final String SERVER_MESSAGE = "존재하지 않는 멤버 조회";
  private static final String CLIENT_MESSAGE = "사용자를 찾지 못했습니다.";

  public MemberNotFoundException(final Long id) {
    super(String.format("%s -> member id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
  }
  public MemberNotFoundException(final String memberId) {
    super(String.format("%s -> member memberId: %s", SERVER_MESSAGE, memberId), CLIENT_MESSAGE, ERROR_CODE);
  }
}
