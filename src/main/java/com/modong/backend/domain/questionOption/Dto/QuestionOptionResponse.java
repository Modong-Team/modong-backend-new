package com.modong.backend.domain.questionOption.Dto;

import com.modong.backend.domain.questionOption.QuestionOption;
import lombok.Getter;

@Getter
public class QuestionOptionResponse {

  private Long id;
  private String value;

  public QuestionOptionResponse(QuestionOption questionOption) {
    this.id = questionOption.getId();
    this.value = questionOption.getContent();
  }
}
