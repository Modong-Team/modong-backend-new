package com.modong.backend.domain.question.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(name = "질문 생성 요청")
public class QuestionRequest {

  @NotNull
  @Schema(description = "질문 유형", example = "1")
  private int questionType;

  @NotBlank
  @Schema(description = "질문 내용", example = "동아리에 들어와서 어떤걸 경험하고 싶나요?")
  private String content;

  @Schema(description = "질문 옵션", nullable = true, type = "List", example = "[질문 1,질문 2, 질문 3, 질문 4]")
  List<String> questionOptionRequest = new ArrayList<>();

}
