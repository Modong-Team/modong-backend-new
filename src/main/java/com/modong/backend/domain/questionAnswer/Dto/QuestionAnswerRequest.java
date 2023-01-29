package com.modong.backend.domain.questionAnswer.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(name = "질문 답변 생성 요청")
public class QuestionAnswerRequest {

  @NotNull  // 질문 ID
  @Schema(description = "질문 ID",  example = "1")
  private Long questionId;

  @Schema(description = "질문 답변", nullable = true, example = "일반 질문 답변입니다!")
  private String answer;

}
