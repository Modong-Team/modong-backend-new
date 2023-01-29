package com.modong.backend.base.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "에러 응답")
public class ErrorResponse {
  @Schema(description = "에러코드")
  private String code;
  @Schema(description = "에러 메세지")
  private String message;

  private ErrorResponse() {
  }

  public ErrorResponse(final String code, final String message) {
    this.code = code;
    this.message = message;
  }
}
