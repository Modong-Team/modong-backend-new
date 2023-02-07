package com.modong.backend.auth.member.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(name = "회원 중복 ID 검사 요청")
public class MemberCheckRequest {

  @NotNull
  @Size(min = 3, max = 20, message = "아이디는 3자리 이상 20자리 이하여야 합니다!")
  @Schema(description = "아이디",  example = "test123")
  private String memberId;

  public MemberCheckRequest(String memberId) {
    this.memberId = memberId;
  }

  public MemberCheckRequest() {
  }
}
