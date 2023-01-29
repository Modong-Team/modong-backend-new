package com.modong.backend.domain.question.Dto;

import com.modong.backend.domain.question.Question;
import com.modong.backend.domain.questionOption.QuestionOption;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(name = "질문 응답")
public class QuestionResponse {

  @Schema(description = "질문 id",  example = "1")
  private Long id;
  @Schema(description = "질문 내용")
  private String content;

  @Schema(description = "질문 타입")
  private int questionType;
  @Schema(description = "질문 옵션 내용 리스트")
  private List<String> options = new ArrayList<>();

  public QuestionResponse(Question question) {
    this.id = question.getId();
    this.content = question.getContent();
    this.questionType = question.getQuestionType().getCode();
    for(QuestionOption questionOption : question.getQuestionOptions()){
      options.add(questionOption.getContent());
    }
//    this.options = question.getQuestionOptions().stream().map(QuestionOptionResponse::new).collect(
//        Collectors.toList()).toString();
  }
}
