package com.modong.backend.domain.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "평가 생성 요청")
public class EvaluationCreateRequest {
  @Schema(description = "평가 점수(소수점1자리까지)",  example = "9.6")
  @NotNull(message = "점수는 필수 항목입니다!")
  private float score;
  @Schema(description = "간단한 평가 내용",  example = "맘에 들어요 ~!@")
  private String comment;

  @Builder
  public EvaluationCreateRequest(float score, String comment) {
    this.score = score;
    this.comment = comment;
  }
}
