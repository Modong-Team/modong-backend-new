package com.modong.backend.auth.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Schema(name = "로그인 요청")
@NoArgsConstructor
public class LoginRequest {
  @NotBlank
  @Schema(description = "아이디",  example = "test123")
  private String memberId;
  @NotBlank
  @Schema(description = "비밀번호",  example = "myPassword1234!")
  private String password;
  public LoginRequest(String memberId, String password) {
    this.memberId = memberId;
    this.password = password;
  }
}
