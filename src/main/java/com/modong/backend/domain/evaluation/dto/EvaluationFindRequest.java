package com.modong.backend.domain.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "평가 조회 요청")
public class EvaluationFindRequest {

  @Schema(description = "지원서 ID",  example = "1")
  @NotNull(message = "지원서 id는 필수 항목입니다!")
  private Long applicationId;
  @Schema(description = "지원자 ID",  example = "2")
  @NotNull(message = "지원자 id는 필수 항목입니다!")
  private Long applicantId;



  @Builder
  public EvaluationFindRequest(Long applicantId, Long applicationId) {
    this.applicantId = applicantId;
    this.applicationId = applicationId;
  }
}
