package com.modong.backend.domain.questionAnswer.Dto;

import com.modong.backend.domain.questionAnswer.QuestionAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "질문 답변")
public class QuestionAnswerResponse {
  @Schema(description = "질문")
  private String Question;
  @Schema(description = "질문 답변 ")
  private String Answer;

  public QuestionAnswerResponse(QuestionAnswer questionAnswer) {
    this.Question = questionAnswer.getQuestion().getContent();
    this.Answer = questionAnswer.getAnswer();
  }
}
