package com.modong.backend.global.exception.club;

import com.modong.backend.global.exception.BadRequestException;

public class ClubNotFoundException extends BadRequestException {

  private static final String ERROR_CODE = "CLUB_NOT_FOUND";
  private static final String SERVER_MESSAGE = "존재하지 않는 동아리 조회";
  private static final String CLIENT_MESSAGE = "동아리를 찾지 못했습니다.";

  public ClubNotFoundException(final Long id) {
    super(String.format("%s -> club id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
  }
  public ClubNotFoundException(final String clubCode) {
    super(String.format("%s -> club code: %s", SERVER_MESSAGE, clubCode), CLIENT_MESSAGE, ERROR_CODE);
  }
}
