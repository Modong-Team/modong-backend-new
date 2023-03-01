package com.modong.backend.domain.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "평가 수정 요청")
public class EvaluationUpdateRequest {
  @Schema(description = "간단한 평가 내용",  example = "맘에 들어요 ~!@")
  private String newComment;
  @Schema(description = "바뀐 평가 점수",  example = "9.6f")
  private float newScore;



  @Builder
  public EvaluationUpdateRequest(String newComment, float newScore) {
    this.newComment = newComment;
    this.newScore = newScore;
  }
}
