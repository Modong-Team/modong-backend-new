package com.modong.backend.Fixtures;

public class MemberFixture {
  public static final Long ID = 1L;
  public static final String MEMBER_ID = "test123";
  public static final String PASSWORD = "myPassword1234!";
  public static final String EMAIL = "email@naver.com";
  public static final String PHONE = "01032343243";
  public static final String NAME = "tester";

  public static final String WRONG_MEMBER_ID = "23"; // 길이가 3미만임.
  public static final String WRONG_PASSWORD = "myPass234"; // 특수 문자 없음

}
