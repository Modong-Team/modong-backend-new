package com.modong.backend.auth.member.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberCheckRequest {

  @NotNull
  @Schema(description = "아이디", required = true, example = "test123")
  private String memberId;

  public MemberCheckRequest(String memberId) {
    this.memberId = memberId;
  }

  public MemberCheckRequest() {
  }
}
