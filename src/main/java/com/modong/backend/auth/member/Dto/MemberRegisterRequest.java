package com.modong.backend.auth.member.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(name = "회원가입 요청")
public class MemberRegisterRequest {
  @NotNull
  @Schema(description = "아이디",  example = "test123")
  private String memberId;
  @NotNull
  @Schema(description = "비밀번호",  example = "myPassword1234!")
  private String password;
  @NotNull
  @Schema(description = "이름",  example = "tester")
  private String name;
  @NotNull
  @Schema(description = "이메일",  example = "email@naver.com")
  private String email;
  @NotNull
  @Schema(description = "휴대폰 번호",  example = "01032343243")
  private String phone;

  @NotNull
  @Schema(description = "동아리 코드",  example = "F1KAO132KA")
  private String clubCode;

  public MemberRegisterRequest(String memberId, String password, String name, String email,
      String phone, String clubCode) {
    this.memberId = memberId;
    this.password = password;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.clubCode = clubCode;
  }
}
