package com.modong.backend.domain.club.Dto;

import com.modong.backend.domain.club.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "동아리 생성 응답")
public class ClubCreateResponse {

  @Schema(description = "동아리 id", example = "1")
  private Long id;
  @Schema(description = "동아리 코드", example = "KaOdf134Rd")
  private String code;

  public ClubCreateResponse(Club club) {
    this.id = club.getId();
    this.code = club.getClubCode();
  }
}
