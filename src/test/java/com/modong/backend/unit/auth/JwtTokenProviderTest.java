package com.modong.backend.unit.auth;

import com.modong.backend.auth.JwtTokenProvider;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
public class JwtTokenProviderTest {
  private JwtTokenProvider tokenProvider;
  private Faker faker = new Faker();

  // 필요한 테스트 인자들
  private Long id;
  private String secretKey;

  private String expiredToken;
  private String validToken;

  @BeforeEach
  public void init(){
    id = faker.random().nextLong(1);
    secretKey = faker.random().hex(128);
    tokenProvider = new JwtTokenProvider(secretKey,1000,1000);
  }

  @DisplayName("사용자의 id를 이용해 만든 토큰의 값은 원래의 id 값과 같아야 한다.")
  @Test
  public void validationPayload(){
    //given
    validToken = tokenProvider.createAccessToken(id);
    //when
    String actual = tokenProvider.getPayload(validToken);
    //then
    assertEquals(String.valueOf(id),actual);
  }

  @DisplayName("토큰의 유효시간이 남았다면 검증함수에서 true 를 반환해야한다.")
  @Test
  public void returnTrueIfTokenIsValid(){
    //given
    validToken = tokenProvider.createToken(id,100000000);
    //when
    boolean actual = tokenProvider.validateToken(validToken);
    //then
    assertEquals(true, actual);
  }

  @DisplayName("토큰의 유효시간이 지났다면 검증함수에서 false 를 반환해야한다.")
  @Test
  public void returnFalseIfTokenIsNotValid(){
    //given
    expiredToken = tokenProvider.createToken(id,0);
    //when
    boolean actual = tokenProvider.validateToken(expiredToken);
    //then
    assertEquals(false, actual);
  }

  @DisplayName("서버의 Secret-Key 와 다른 값으로 만든 토큰을 요청으로 받으면 검증함수에서 false 를 반환해야 한다.")
  @Test
  public void returnFalseIfSecretKeyIsDifferent (){
    //given
    final String fakeKey = faker.random().hex(128);
    final JwtTokenProvider fakeProvider = new JwtTokenProvider(fakeKey,1000,1000);
    final String fakeToken = fakeProvider.createAccessToken(id);
    //when
    boolean actual = tokenProvider.validateToken(fakeToken);
    //then
    assertEquals(false,actual);
  }


//  @DisplayName("유효한 토큰인지 검사한다.")


}
