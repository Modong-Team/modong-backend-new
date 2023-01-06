package com.modong.backend.Enum;

public enum ApplicantStatus {
  FAIL(1),ACCEPT(2),APPLICATION(3),INTERVIEW(4),SUCCESS(5);

  private int code;

  ApplicantStatus(int code) {
    this.code = code;
  }

  public static ApplicantStatus valueOf(int code) {
    ApplicantStatus applicantStatus = null;
    switch (code) {
      case 1:
        applicantStatus = ApplicantStatus.FAIL;
        break;
      case 2:
        applicantStatus = ApplicantStatus.ACCEPT;
        break;
      case 3:
        applicantStatus = ApplicantStatus.APPLICATION;
        break;
      case 4:
        applicantStatus = ApplicantStatus.INTERVIEW;
        break;
      case 5:
        applicantStatus = ApplicantStatus.SUCCESS;
        break;
      default:
        break;
    }
    return applicantStatus;
  }

  public int getCode() {
    return this.code;
  }
}
