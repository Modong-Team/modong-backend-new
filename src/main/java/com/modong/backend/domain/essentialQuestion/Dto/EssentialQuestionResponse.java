package com.modong.backend.domain.essentialQuestion.Dto;

import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "필수 질문 응답")
public class EssentialQuestionResponse {

  @Schema(description = "필수 질문 id",  example = "1")
  private Long id;

  @Schema(description = "필수 질문 내용")
  private String content;
  @Schema(description = "필수 여부")
  private boolean isRequire;


  public EssentialQuestionResponse(EssentialQuestion essentialQuestion) {
    this.id = essentialQuestion.getId();
    this.content = essentialQuestion.getContent();
    this.isRequire = essentialQuestion.isRequire();
  }
}
