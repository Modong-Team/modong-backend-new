package com.modong.backend.Enum;

import lombok.Getter;

@Getter
public enum CustomCode {
  //공통
  SUCCESS_GET("조회 성공", "SC001"), SUCCESS_GET_LIST("목록 조회 성공", "SC002"),
  SUCCESS_CREATE("저장 성공", "SC003"), SUCCESS_UPDATE("수정 성공", "SC004"),
  SUCCESS_DELETE("삭제 성공", "SC005"), SUCCESS_LOGIN("로그인 성공", "SC006" ),
  SUCCESS_GET_TOKEN("토큰 재발행 성공", "SC007" ), SUCCESS_CHECK("중복 확인 성공","SC008" ),

  ERROR_SERVER("서버에서 오류가 발생했습니다.", "EC000"),
  ERROR_REQ("요청 값이 잘못되었습니다.","EC002"),
  ERROR_REQ_PARAM_ID("존재하지 않는 ID 입니다.", "EC003");

  private String message;
  private String code;

  CustomCode(String message, String code) {
    this.message = message;
    this.code = code;
  }
}
