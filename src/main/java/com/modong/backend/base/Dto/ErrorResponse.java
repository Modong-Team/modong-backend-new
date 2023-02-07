package com.modong.backend.base.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "에러 응답")
public class ErrorResponse {

  private Date timestamp = Calendar.getInstance().getTime();
  @Schema(description = "에러코드")
  private String code;
  @Schema(description = "에러 메세지")
  private String message;

  @Schema(description = "HTTP 상태 값")
  private int status; // HTTP 상태 값 저장 400, 404, 500 등..



  @Builder
  public ErrorResponse(String code, String message, int status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
