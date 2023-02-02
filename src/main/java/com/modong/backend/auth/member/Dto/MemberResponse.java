package com.modong.backend.auth.member.Dto;

import com.modong.backend.auth.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
@Getter
@Schema(name = "회원 정보 응답")
public class MemberResponse {

  @Schema(description = "회원 id")
  private Long id;
  @Schema(description = "아이디",  example = "test123")
  private String memberId;
  @Schema(description = "이름",  example = "tester")
  private String name;
  @Schema(description = "이메일",  example = "email@naver.com")
  private String email;
  @Schema(description = "휴대폰 번호",  example = "01032343243")
  private String phone;

  public MemberResponse(Member member) {
    this.id = member.getId();
    this.memberId = member.getMemberId();
    this.name = member.getName();
    this.email = member.getEmail();
    this.phone = member.getPhone();
  }
}
