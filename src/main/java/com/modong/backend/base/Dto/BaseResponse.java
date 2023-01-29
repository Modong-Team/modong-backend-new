package com.modong.backend.base.Dto;

import com.modong.backend.Enum.CustomCode;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Getter;

@Getter
@Schema(name = "기본 반환 형식")
public class BaseResponse {
  @Schema(description = "상태값", example = "200")
  private int status;
  @Schema(description = "응답 메시지", example = "조회 성공")
  private String message;
  @Schema(description = "반환 코드 값", example = "SC001")
  private String code;
  @Schema(description = "반환 데이터", nullable=true)
  private Object data;

  public BaseResponse(int status, CustomCode customCode) {
    this.status = status;
    this.message = customCode.getMessage();
    this.code = customCode.getCode();
  }

  public BaseResponse(Object body, int status, CustomCode customCode) {
    this.data = body;
    this.status = status;
    this.message = customCode.getMessage();
    this.code = customCode.getCode();
  }
}
