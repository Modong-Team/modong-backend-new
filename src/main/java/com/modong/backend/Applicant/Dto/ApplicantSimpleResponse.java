package com.modong.backend.Applicant.Dto;

import static com.modong.backend.Utils.DateUtils.asDate;

import com.modong.backend.Applicant.Applicant;
import java.util.Date;
import lombok.Getter;

@Getter
public class ApplicantSimpleResponse {
  private Long id;
  private String name;
  private float rate;
  private String status;
  private Date submitDate;

  private boolean isFail;

  public ApplicantSimpleResponse(Applicant applicant) {
    this.id = applicant.getId();
    this.name = applicant.getName();
    this.rate = applicant.getRate();
    this.status = applicant.getApplicantStatus().toString();
    this.submitDate = asDate(applicant.getCreateDate());
    this.isFail = applicant.isFail();
  }
}
