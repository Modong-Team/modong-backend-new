package com.modong.backend.auth.member.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(name = "회원가입 요청")
public class MemberRegisterRequest {
  @NotNull
  @Size(min = 3, max = 20, message = "아이디는 3자리 이상 20자리 이하여야 합니다!")
  @Schema(description = "아이디",  example = "test123")
  private String memberId;
  @NotNull
  @Size(min = 6, max = 20, message = "비밀번호는 6자리 이상 20자리 이하여야 합니다!")
  @Schema(description = "비밀번호",  example = "myPassword1234!")
  private String password;
  @NotNull
  @Schema(description = "이름",  example = "tester")
  private String name;
  @Email(message = "이메일 형식을 확인해주세요")
  @Schema(description = "이메일",  example = "email@naver.com")
  private String email;
  @NotNull
  @Schema(description = "휴대폰 번호",  example = "01032343243")
  private String phone;

  @NotNull
  @Size(min = 10,max = 10,message = "동아리 코드는 10자리여야 합니다 !")
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
