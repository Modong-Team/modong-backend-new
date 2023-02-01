package com.modong.backend.domain.applicant.Dto;

import com.modong.backend.domain.applicant.Applicant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Schema(name = "페이징 - 지원자 간단 조회")
public class PageApplicantsResponse {

  @Schema(description = "지원자 간단 조회 객체를 감싼 페이징 객체")
  private final Page<ApplicantSimpleResponse> result;

  public PageApplicantsResponse(Page<Applicant> result) {
    this.result = result.map(ApplicantSimpleResponse::new);
  }
}
