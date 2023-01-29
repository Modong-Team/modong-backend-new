package com.modong.backend.domain.questionOption.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.Null;
import lombok.Getter;

@Getter
public class QuestionOptionRequest {

  @Schema(description = "선택하는 질문의 옵션 값들", nullable = true, example = "선택 질문의 값입니다.")
  @Null
  private List<String> values;

}
