package com.modong.backend.domain.Application.Dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import java.util.List;

@Getter
public class ApplicationRequest {

  @NotNull
  @Schema(description = "동아리 ID", required = true, example = "1")
  private Long clubId;

  @NotBlank
  @Schema(description = "지원서 제목", required = true, example = "동아리 지원서 ver 1.0")
  private String title;

  @Schema(description = "지원자 접수할 링크 아이디", required = true, example = "uH9wk72MTr")
  private String urlId;

  @Schema(description = "필수질문 ID 리스트", required = false,  example = "[1,2,3]")
  private List<Long> essentialQuestionIds = new ArrayList<>();



}
