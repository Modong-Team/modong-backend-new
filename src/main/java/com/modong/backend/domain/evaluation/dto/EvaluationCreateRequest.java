package com.modong.backend.domain.evaluation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EvaluationCreateRequest {

  private Long applicationId;
  private Long applicantId;

  private float score;

  @Builder
  public EvaluationCreateRequest(Long applicationId, Long applicantId, float score) {
    this.applicationId = applicationId;
    this.applicantId = applicantId;
    this.score = score;
  }
}
