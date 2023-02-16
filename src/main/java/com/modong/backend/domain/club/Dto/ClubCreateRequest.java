package com.modong.backend.domain.club.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(name = "동아리 생성 요청")
public class ClubCreateRequest {

  @NotBlank
  @Schema(description = "동아리 이름", example = "testClub")
  private String name;
  @NotBlank
  @Schema(description = "동아리 프로필 이미지 링크", example = "https://avatars.githubusercontent.com/u/38587274?v=4")
  private String profileImgUrl;

  @NotBlank
  @Schema(description = "동아리 모집 시작 날짜", example = "2023-03-01")
  private String startDate;
  @NotBlank
  @Schema(description = "동아리 모집 마감 날짜", example = "2023-03-31")
  private String endDate;


  @Builder
  public ClubCreateRequest(String name, String profileImgUrl, String startDate, String endDate) {
    this.name = name;
    this.profileImgUrl = profileImgUrl;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
