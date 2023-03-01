package com.modong.backend.domain.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "메모 생성 요청")
public class MemoCreateRequest {
  @Schema(description = "지원자 ID",  example = "2")
  @NotNull(message = "지원자 id는 필수 항목입니다!")
  private Long applicantId;
  @Schema(description = "메모 내용",  example = "메모입니다~~@~!")
  @NotNull(message = "메모할 내용은 필수 항목입니다!")
  private String content;

  @Builder
  public MemoCreateRequest(Long applicationId, Long applicantId, String content) {
    this.applicantId = applicantId;
    this.content = content;
  }

}
