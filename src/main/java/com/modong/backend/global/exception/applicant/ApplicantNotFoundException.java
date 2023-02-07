package com.modong.backend.global.exception.applicant;

import com.modong.backend.global.exception.NotFoundException;

public class ApplicantNotFoundException extends NotFoundException {

  private static final String ERROR_CODE = "APPLICANT_NOT_FOUND";
  private static final String SERVER_MESSAGE = "존재하지 않는 지원자 조회";
  private static final String CLIENT_MESSAGE = "지원자를 찾지 못했습니다.";

  public ApplicantNotFoundException(final Long id) {
    super(String.format("%s -> applicant id: %d", SERVER_MESSAGE, id), CLIENT_MESSAGE, ERROR_CODE);
  }
}
