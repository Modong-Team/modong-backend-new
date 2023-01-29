package com.modong.backend.domain.club;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClubCheckRequest {

  private String ClubCode;

  public ClubCheckRequest(String clubCode) {
    ClubCode = clubCode;
  }
}
