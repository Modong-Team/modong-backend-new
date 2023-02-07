package com.modong.backend.domain.questionAnswer.Dto;

import com.modong.backend.domain.questionAnswer.QuestionAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "질문 답변")
public class QuestionAnswerResponse {

  @Schema(description = "질문 id")
  private Long id;

  @Schema(description = "질문")
  private String question;
  @Schema(description = "질문 답변 ")
  private String answer;

  public QuestionAnswerResponse(QuestionAnswer questionAnswer) {
    this.id = questionAnswer.getQuestion().getId();
    this.question = questionAnswer.getQuestion().getContent();
    this.answer = questionAnswer.getAnswer();
  }
}
