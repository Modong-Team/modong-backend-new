package com.modong.backend.auth.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "토큰 응답")
public class TokenResponse {
  @Schema(description = "지원서 ID", example = "1")
  private Long memberId;
  @Schema(description = "access 토큰")
  private String accessToken;
  @Schema(description = "refresh 토큰")
  private String refreshToken;

  public TokenResponse(Long memberId, String accessToken, String refreshToken) {
    this.memberId = memberId;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
