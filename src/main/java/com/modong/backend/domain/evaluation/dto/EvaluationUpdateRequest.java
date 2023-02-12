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

  @Schema(description = "평가 ID",  example = "1")
  @NotNull(message = "평가 id는 필수 항목입니다!")
  private Long evaluationId;
  @Schema(description = "간단한 평가 내용",  example = "맘에 들어요 ~!@")
  private String newComment;
  @Schema(description = "바뀐 평가 점수",  example = "9.6f")
  private float newScore;
  @Schema(description = "지원자 ID",  example = "2")
  @NotNull(message = "지원자 id는 필수 항목입니다!")
  private Long applicantId;



  @Builder
  public EvaluationUpdateRequest(Long evaluationId,String newComment, float newScore, Long applicantId) {
    this.newComment = newComment;
    this.evaluationId = evaluationId;
    this.newScore = newScore;
    this.applicantId = applicantId;
  }
}
