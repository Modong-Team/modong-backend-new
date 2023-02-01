package com.modong.backend.auth.member.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Schema(name = "회원 중복 ID 검사 요청")
@NoArgsConstructor
public class MemberCheckRequest {

  @NotNull
  @Schema(description = "아이디",  example = "test123")
  private String memberId;

  public MemberCheckRequest(String memberId) {
    this.memberId = memberId;
  }
}
