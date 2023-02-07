package com.modong.backend.domain.application.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "지원서 생성 요청")
public class ApplicationUpdateRequest {

  @NotBlank
  @Schema(description = "지원서 제목",  example = "동아리 지원서 ver 1.0")
  private String title;

  @Schema(description = "필수질문 ID 리스트", nullable = true,  example = "[1,2,3]")
  private List<Long> essentialQuestionIds = new ArrayList<>();


  @Builder
  public ApplicationUpdateRequest(String title, List<Long> essentialQuestionIds) {
    this.title = title;
    this.essentialQuestionIds = essentialQuestionIds;
  }
}
