package com.modong.backend.unit.auth;

import com.modong.backend.Fixtures.MemberFixture;
import com.modong.backend.auth.AuthService;
import com.modong.backend.auth.Dto.LoginRequest;
import com.modong.backend.auth.Dto.TokenRequest;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.refreshToken.RefreshToken;
import com.modong.backend.global.exception.auth.PasswordMismatchException;
import com.modong.backend.global.exception.auth.RefreshTokenNotFoundException;
import com.modong.backend.global.exception.auth.RefreshTokenNotValidException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import com.modong.backend.unit.base.ServiceTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static com.modong.backend.Fixtures.AuthFixture.REFRESH_TOKEN;
import static com.modong.backend.Fixtures.AuthFixture.WRONG_REFRESH_TOKEN;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.PASSWORD;
import static com.modong.backend.Fixtures.MemberFixture.WRONG_PASSWORD;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthServiceTest extends ServiceTest {

  @Autowired
  private AuthService authService;

  private Member member;
  @BeforeEach
  public void init(){
    member = new Member(memberRegisterRequest);
    member.setEncodedPassword(passwordEncoder.encode(PASSWORD));
    ReflectionTestUtils.setField(member,"id",MemberFixture.ID);
  }

  // 로그인 테스트
  @DisplayName("로그인 실패 - MemberId를 가진 사용자가 없을경우 MemberNotFoundException 이 발생한다. ")
  @Test
  public void throwExceptionIfMemberIdNotExists(){
    //given, when
    LoginRequest loginRequest = new LoginRequest(MEMBER_ID,PASSWORD);

    given(memberRepository.findByMemberId(anyString()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> authService.login(loginRequest))
        .isInstanceOf(MemberNotFoundException.class);
  }
  @DisplayName("로그인 실패 - 비밀번호가 다를 경우 PasswordMissMatchException 이 발생한다. ")
  @Test
  public void throwExceptionIfPassWordNotMatch(){
    //given, when
    LoginRequest loginRequest = new LoginRequest(MEMBER_ID,WRONG_PASSWORD);

    given(memberRepository.findByMemberId(anyString()))
        .willReturn(Optional.of(member));

    //then
    assertThatThrownBy(() -> authService.login(loginRequest))
        .isInstanceOf(PasswordMismatchException.class);
  }

  @DisplayName("로그인 성공 - 최초 로그인시 토큰이 발급되어야 한다.")
  @Test
  public void returnTokenIfLoginRequestIsValid(){
    //given
    LoginRequest loginRequest = new LoginRequest(MEMBER_ID,PASSWORD);

    given(memberRepository.findByMemberId(anyString()))
        .willReturn(Optional.of(member));

    //when
    TokenResponse tokenResponse = authService.login(loginRequest);

    //then
    assertNotNull(tokenResponse);
  }

  //
  @DisplayName("로그인 성공 - 이미 저장된 RefreshToken 이 있을 경우 저장된 토큰을 반환해야 한다.")
  @Test
  public void returnStoredTokenIfRefreshTokenIsStored(){
    //given
    LoginRequest loginRequest = new LoginRequest(MEMBER_ID,PASSWORD);

    given(memberRepository.findByMemberId(anyString()))
        .willReturn(Optional.of(member));
    given(refreshTokenRepository.findByMemberId(anyLong()))
        .willReturn(Optional.of(new RefreshToken(REFRESH_TOKEN)));

    //when
    TokenResponse tokenResponse = authService.login(loginRequest);
    System.out.println(tokenResponse.getAccessToken());
    //then
    assertThat(REFRESH_TOKEN).isEqualTo(tokenResponse.getRefreshToken());
  }
  //토큰 재발급 테스트

  @DisplayName("토큰 재발급 성공 - RefreshToken 이 유효하다면 새로운 AccessToken 과 RefreshToken 을 반환한다.")
  @Test
  public void returnNewTokenIfRefreshTokenIsValid(){
    //given
    TokenRequest tokenRequest = new TokenRequest(MemberFixture.ID,REFRESH_TOKEN);

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(true);
    given(refreshTokenRepository.findByMemberId(MemberFixture.ID))
        .willReturn(Optional.of(new RefreshToken(REFRESH_TOKEN,MemberFixture.ID)));

    //when
    TokenResponse tokenResponse = authService.createAccessToken(tokenRequest);

    //then
    assertNotNull(tokenResponse);
    assertThat(tokenRequest.getRefreshToken()).isNotEqualTo(tokenResponse.getRefreshToken());
  }

  @DisplayName("토큰 재발급 실패 - id에 등록된 멤버가 DB에 존재하지 않으면 MemberNotFoundException 이 발생한다. ")
  @Test
  public void throwExceptionIfMemberIsNotFound(){
    //given,when
    TokenRequest tokenRequest = new TokenRequest(MemberFixture.ID,REFRESH_TOKEN);
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> authService.createAccessToken(tokenRequest))
        .isInstanceOf(MemberNotFoundException.class);
  }
  @DisplayName("토큰 재발급 실패 - RefreshToken 이 유효하지 않으면 RefreshTokenNotValidException 이 발생한다. ")
  @Test
  public void throwExceptionIfRefreshTokenIsNotValid(){
    //given, when
    TokenRequest tokenRequest = new TokenRequest(MemberFixture.ID,REFRESH_TOKEN);

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(false);

    //then
    assertThatThrownBy(() -> authService.createAccessToken(tokenRequest))
        .isInstanceOf(RefreshTokenNotValidException.class);
  }
  @DisplayName("토큰 재발급 실패 - RefreshToken 이 DB에 저장된 토큰과 같지 않으면 RefreshTokenNotValidException 이 발생한다. ")
  @Test
  public void throwExceptionIfRefreshTokenIsNotMatch(){
    //given, when
    TokenRequest tokenRequest = new TokenRequest(MemberFixture.ID,WRONG_REFRESH_TOKEN);

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(true);
    given(refreshTokenRepository.findByMemberId(anyLong()))
        .willReturn(Optional.of(new RefreshToken(REFRESH_TOKEN,MemberFixture.ID)));

    //then
    assertThatThrownBy(() -> authService.createAccessToken(tokenRequest))
        .isInstanceOf(RefreshTokenNotValidException.class);
  }
  @DisplayName("토큰 재발급 실패 - RefreshToken 이 DB에 존재하지 않으면 RefreshTokenNotFoundException 이 발생한다. ")
  @Test
  public void throwExceptionIfRefreshTokenIsNotFound(){
    //given,when
    TokenRequest tokenRequest = new TokenRequest(MemberFixture.ID,REFRESH_TOKEN);

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(true);
    given(refreshTokenRepository.findByMemberId(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> authService.createAccessToken(tokenRequest))
        .isInstanceOf(RefreshTokenNotFoundException.class);
  }

}
