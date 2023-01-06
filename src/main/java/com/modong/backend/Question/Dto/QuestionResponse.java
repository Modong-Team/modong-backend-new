package com.modong.backend.Question.Dto;

import com.modong.backend.QuestionOption.Dto.QuestionOptionResponse;
import com.modong.backend.Question.Question;
import com.modong.backend.QuestionOption.QuestionOption;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.Getter;
import java.util.List;

@Getter
public class QuestionResponse {

  private Long id;
  private String content;

  private int questionType;

  private List<String> options = new ArrayList<>();

  public QuestionResponse(Question question) {
    this.id = question.getId();
    this.content = question.getContent();
    this.questionType = question.getQuestionType().getCode();
    for(QuestionOption questionOption : question.getQuestionOptions()){
      options.add(questionOption.getValue());
    }
//    this.options = question.getQuestionOptions().stream().map(QuestionOptionResponse::new).collect(
//        Collectors.toList()).toString();
  }
}
