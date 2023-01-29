package com.modong.backend.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class UUIDGenerateUtils {

  // 10자리의 UUID 생성 (알파벳 대소문자 및 숫자 사용)
  public static String makeShortUUID() {
    return RandomStringUtils.random(10, 33, 125, true, true);
  }

}