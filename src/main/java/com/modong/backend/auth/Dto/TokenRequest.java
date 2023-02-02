package com.modong.backend.auth.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "토큰 재발행 요청")
public class TokenRequest {
  @NotNull
  @Schema(description = "로그인때 발급된 refresh 토큰")
  private String refreshToken;

  public TokenRequest(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public TokenRequest() {
  }
}
