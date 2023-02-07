package com.modong.backend.domain.club.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(name = "동아리 존재 여부 검사 요청")
public class ClubCheckRequest {

  @NotNull
  @Schema(description = "동아리 코드", example = "F1KAO132KA")
  @Size(min = 10,max = 10,message = "동아리 코드는 10자리여야 합니다 !")
  private String clubCode;

  @Builder
  public ClubCheckRequest(String clubCode) {
    this.clubCode = clubCode;
  }

  public ClubCheckRequest() {
  }
}
