package com.modong.backend.domain.applicant.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Schema(name = "지원자들 조회 요청")
public class SearchApplicantRequest {

  @Schema(description = "지원자 상태", example = "ACCEPT(2),APPLICATION(3),INTERVIEW(4),SUCCESS(5)")
  private int applicantStatusCode;

}
