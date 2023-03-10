package com.modong.backend.domain.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "메모 조회 요청")
public class MemoFindRequest {

  @Schema(description = "지원자 ID",  example = "2")
  @NotNull(message = "지원자 id는 필수 항목입니다!")
  private Long applicantId;

  @Builder
  public MemoFindRequest(Long applicantId, Long applicationId) {
    this.applicantId = applicantId;
  }
}
