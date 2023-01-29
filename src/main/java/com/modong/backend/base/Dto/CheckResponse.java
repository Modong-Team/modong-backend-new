package com.modong.backend.base.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "검사 응답")
public class CheckResponse {
  @Schema(description = "검사 결과")
  private boolean result;

  public CheckResponse(boolean result) {
    this.result = result;
  }
}
