package com.modong.backend.domain.application.Dto;

import com.modong.backend.domain.application.Application;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApplicationSimpleResponse {
  @Schema(description = "지원서 id", example = "1")
  private Long id;
  @Schema(description = "지원서 제목", example = "동아리 지원서 ver 1.0")
  private String title;
  @Schema(description = "지원서 링크 아이디", example = "9CJOj71S1o")
  private String urlId;

  public ApplicationSimpleResponse(Application application) {
    this.id = application.getId();
    this.title = application.getTitle();
    this.urlId = application.getUrlId();
  }
}
