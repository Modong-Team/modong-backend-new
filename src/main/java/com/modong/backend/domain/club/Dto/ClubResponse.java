package com.modong.backend.domain.club.Dto;
import com.modong.backend.domain.club.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "동아리 조회 응답")
public class ClubResponse {
  @Schema(description = "동아리 id", example = "1")
  private Long id;
  @Schema(description = "동아리 이름", example = "소리터")
  private String name;
  @Schema(description = "동아리 이름", example = "소리터")
  private String profileImgUrl;
  @Schema(description = "동아리 코드", example = "KaOdf134Rd")
  private String clubCode;
  public ClubResponse(Club club) {
    this.id = club.getId();
    this.name = club.getName();
    this.profileImgUrl = club.getProfileImgUrl();
    this.clubCode = club.getClubCode();
  }
}
