package com.modong.backend.domain.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "메모 수정 요청")
public class MemoUpdateRequest {
  @Schema(description = "메모 내용",  example = "메모입니다~~@~!")
  @NotNull(message = "메모할 내용은 필수 항목입니다!")
  private String content;

  @Builder
  public MemoUpdateRequest(String content) {
    this.content = content;
  }

}
