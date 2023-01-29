package com.modong.backend.base.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "객체 저장 후 식별 id 응답")
public class SavedId {
  @Schema(description = "id")
  private Long id;

  public SavedId(Long id) {
    this.id = id;
  }
}
