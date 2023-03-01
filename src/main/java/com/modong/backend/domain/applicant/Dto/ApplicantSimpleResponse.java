package com.modong.backend.domain.applicant.Dto;

import static com.modong.backend.utils.DateUtils.asDate;

import com.modong.backend.domain.applicant.Applicant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Getter;

@Getter
@Schema(name = "지원자 간단 조회")
public class ApplicantSimpleResponse {
  @Schema(description = "지원서 id", example = "1")
  private Long ApplicationId;
  @Schema(description = "지원자 id",  example = "1")
  private Long id;
  @Schema(description = "이름",  example = "아무개")
  private String name;
  @Schema(description = "평가 받은 점수",  example = "7.8")
  private float rate;
  @Schema(description = "상태",  example = "FAIL(1),ACCEPT(2),APPLICATION(3),INTERVIEW(4),SUCCESS(5)")
  private String status;
  @Schema(description = "제출일",  example = "2023-01-05T02:03:53.000+00:00")
  private Date submitDate;
  @Schema(description = "탈락 여부",  example = "true/false")
  private boolean isFail;

  @Schema(description = "평가자 수",  example = "1")
  private Long numOfEvaluator;

  public ApplicantSimpleResponse(Applicant applicant) {
    this.ApplicationId = applicant.getApplication().getId();
    this.id = applicant.getId();
    this.name = applicant.getName();
    this.rate = getRoundedRate(applicant.getRate()).floatValue();
    this.status = applicant.getApplicantStatus().toString();
    this.submitDate = asDate(applicant.getCreateDate());
    this.isFail = applicant.isFail();
    this.numOfEvaluator = applicant.getCountOfEvaluator();
  }

  private Double getRoundedRate(Float rate){
    return (Math.round(rate*10)/10.0) ;
  }

}
