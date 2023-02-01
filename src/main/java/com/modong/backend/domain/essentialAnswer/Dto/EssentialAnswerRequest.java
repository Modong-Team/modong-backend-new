package com.modong.backend.domain.essentialAnswer.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(name = "필수 질문 요청")
@NoArgsConstructor
public class EssentialAnswerRequest {


  @NotNull // 필수 질문 ID
  @Schema(description = "필수 질문 ID",  example = "1")
  private Long essentialQuestionId;

  //필수 질문 답변
  @NotBlank
  @Schema(description = "필수 질문 답변",  example = "필수 질문 답변입니다!")
  private String answer;

}
