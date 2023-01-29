package com.modong.backend.domain.essentialAnswer.Dto;

import com.modong.backend.domain.essentialAnswer.EssentialAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "필수 질문 응답")
public class EssentialAnswerResponse {
  @Schema(description = "필수 질문")
  private String essentialQuestion;
  @Schema(description = "필수 질문 답변")
  private String essentialAnswer;

  public EssentialAnswerResponse(EssentialAnswer essentialAnswer) {
    this.essentialQuestion = essentialAnswer.getEssentialQuestion().getContent();
    this.essentialAnswer = essentialAnswer.getAnswer();
  }
}
