package com.modong.backend.Applicant.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangeApplicantStatusRequest {

  @NotNull
  @Schema(description = "변경하고 싶은 지원자 상태 코드", required = true, example = "2")
  private int applicantStatusCode;
}
