package com.modong.backend.Question.Dto;

import com.modong.backend.QuestionOption.Dto.QuestionOptionRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import lombok.Getter;
import java.util.List;

@Getter
public class QuestionRequest {

  @NotNull
  @ApiModelProperty(value = "질문 유형", required = true, example = "1")
  private int questionType;

  @NotBlank
  @ApiModelProperty(value = "질문 내용", required = true, example = "동아리에 들어와서 어떤걸 경험하고 싶나요?")
  private String content;

  @ApiModelProperty(value = "질문 옵션", required = false, dataType = "List", example = "[질문 1,질문 2, 질문 3, 질문 4]")
  List<String> questionOptionRequest;

}
