package com.modong.backend.domain.essentialQuestion.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(name = "필수 질문 요청")
@NoArgsConstructor
public class EssentialQuestionRequest {
  @Schema(description = "지원자 id",  example = "1")
  @NotNull
  private Long id;
  @NotBlank
  private String content;
  @NotNull
  private boolean isRequire;

}
