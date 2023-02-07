package com.modong.backend.base.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "존재 검사 응답")
public class ExistsResponse {

  @Schema(description = "존재 검사 결과")
  private boolean exists;

  public ExistsResponse(boolean result) {
    this.exists = result;
  }
}
