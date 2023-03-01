package com.modong.backend.base.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "중복 검사 응답")
public class DuplicatedCheckResponse {
  @Schema(description = "중복 검사 결과")
  private boolean duplicated;

  public DuplicatedCheckResponse(boolean result) {
    this.duplicated = result;
  }
}
