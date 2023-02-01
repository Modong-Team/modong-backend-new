package com.modong.backend.domain.club.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Schema(name = "클럽 코드 검사 요청")
@NoArgsConstructor
public class ClubCheckRequest {

  @NotBlank
  @Schema(description = "클럽코드",  example = "h4RdgpIqWj")
  private String ClubCode;

  public ClubCheckRequest(String clubCode) {
    ClubCode = clubCode;
  }
}
