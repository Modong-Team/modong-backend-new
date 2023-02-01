package com.modong.backend.domain.applicant.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "지원자 상태 변경 요청")
public class ChangeApplicantStatusRequest {

  @NotNull
  @Schema(description = "변경하고 싶은 지원자 상태 코드",  example = "FAIL(1),ACCEPT(2),APPLICATION(3),INTERVIEW(4),SUCCESS(5)")
  private int applicantStatusCode;
}
