package com.modong.backend.domain.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "메모 삭제 요청")
public class MemoDeleteRequest {

  @Schema(description = "메모 ID",  example = "1")
  @NotNull(message = "메모 id는 필수 항목입니다!")
  private Long memoId;

  @Builder
  public MemoDeleteRequest(Long memoId) {
    this.memoId = memoId;
  }


}