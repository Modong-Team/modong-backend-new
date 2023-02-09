package com.modong.backend.domain.application.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Schema(name = "지원서 생성 요청")
@NoArgsConstructor
public class ApplicationCreateRequest {

  @NotNull
  @Schema(description = "동아리 ID",  example = "1")
  private Long clubId;

  @NotNull
  @Schema(description = "지원서 제목",  example = "동아리 지원서 ver 1.0")
  private String title;

  @NotNull
  @Schema(description = "지원자 접수할 링크 아이디",  example = "uH9wk72MTr")
  private String urlId;

  @Schema(description = "필수질문 ID 리스트", nullable = true,  example = "[1,2,3]")
  private List<Long> essentialQuestionIds = new ArrayList<>();


  @Builder
  public ApplicationCreateRequest(Long clubId, String title, String urlId,
      List<Long> essentialQuestionIds) {
    this.clubId = clubId;
    this.title = title;
    this.urlId = urlId;
    this.essentialQuestionIds = essentialQuestionIds;
  }
}
