package com.modong.backend.domain.club.Dto;
import com.modong.backend.domain.club.Club;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(name = "동아리 조회 응답")
public class ClubResponse {
  @Schema(description = "동아리 id", example = "1")
  private Long id;
  @Schema(description = "동아리 이름", example = "소리터")
  private String name;
  @Schema(description = "동아리 프로필 이미지 링크", example = "https://avatars.githubusercontent.com/u/38587274?v=4")
  private String profileImgUrl;
  @Schema(description = "동아리 코드", example = "KaOdf134Rd")
  private String clubCode;
  @Schema(description = "동아리 모집 시작 날짜", example = "2023-03-01")
  private String startDate;
  @Schema(description = "동아리 모집 마감 날짜", example = "2023-03-31")
  private String endDate;
  @Schema(description = "동아리 총 인원 수", example = "5")
  private Long numOfMember;
  public ClubResponse(Club club) {
    this.id = club.getId();
    this.name = club.getName();
    this.profileImgUrl = club.getProfileImgUrl();
    this.clubCode = club.getClubCode();
    this.startDate = club.getStartDate();
    this.endDate = club.getEndDate();
    this.numOfMember = club.getCountOfMember();
  }
}
