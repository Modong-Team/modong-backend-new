package com.modong.backend.unit.auth;

import com.modong.backend.Fixtures.MemberFixture;
import com.modong.backend.auth.AuthService;
import com.modong.backend.auth.Dto.LoginRequest;
import com.modong.backend.auth.Dto.TokenRequest;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.refreshToken.RefreshToken;
import com.modong.backend.global.exception.auth.PasswordMismatchException;
import com.modong.backend.global.exception.auth.TokenNotFoundException;
import com.modong.backend.global.exception.auth.TokenNotValidException;
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
import static com.modong.backend.Fixtures.ClubFixture.CLUB_ID;
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
    member = new Member(memberRegisterRequest, CLUB_ID);
    member.setEncodedPassword(passwordEncoder.encode(PASSWORD));
    ReflectionTestUtils.setField(member,"id",MemberFixture.ID);
  }

  // ????????? ?????????
  @DisplayName("????????? ?????? - MemberId??? ?????? ???????????? ???????????? MemberNotFoundException ??? ????????????. ")
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
  @DisplayName("????????? ?????? - ??????????????? ?????? ?????? PasswordMissMatchException ??? ????????????. ")
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

  @DisplayName("????????? ?????? - ?????? ???????????? ????????? ??????????????? ??????.")
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
  @DisplayName("????????? ?????? - ?????? ????????? RefreshToken ??? ?????? ?????? ????????? ????????? ???????????? ??????.")
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
  //?????? ????????? ?????????

  @DisplayName("?????? ????????? ?????? - RefreshToken ??? ??????????????? ????????? AccessToken ??? RefreshToken ??? ????????????.")
  @Test
  public void returnNewTokenIfRefreshTokenIsValid(){
    //given
    TokenRequest tokenRequest = new TokenRequest(REFRESH_TOKEN);

    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(true);
    given(jwtTokenProvider.getPayload(anyString()))
        .willReturn(MemberFixture.ID.toString());
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(refreshTokenRepository.findByMemberId(MemberFixture.ID))
        .willReturn(Optional.of(new RefreshToken(REFRESH_TOKEN,MemberFixture.ID)));

    //when
    TokenResponse tokenResponse = authService.createAccessToken(tokenRequest);

    //then
    assertNotNull(tokenResponse);
    assertThat(tokenRequest.getRefreshToken()).isNotEqualTo(tokenResponse.getRefreshToken());
  }

  @DisplayName("?????? ????????? ?????? - id??? ????????? ????????? DB??? ???????????? ????????? MemberNotFoundException ??? ????????????. ")
  @Test
  public void throwExceptionIfMemberIsNotFound(){
    //given,when
    TokenRequest tokenRequest = new TokenRequest(REFRESH_TOKEN);

    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(true);
    given(jwtTokenProvider.getPayload(anyString()))
        .willReturn(MemberFixture.ID.toString());
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.empty());


    //then
    assertThatThrownBy(() -> authService.createAccessToken(tokenRequest))
        .isInstanceOf(MemberNotFoundException.class);
  }
  @DisplayName("?????? ????????? ?????? - RefreshToken ??? ???????????? ????????? TokenNotValidException ??? ????????????. ")
  @Test
  public void throwExceptionIfRefreshTokenIsNotValid(){
    //given, when
    TokenRequest tokenRequest = new TokenRequest(REFRESH_TOKEN);

    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(false);
    given(jwtTokenProvider.getPayload(anyString()))
        .willReturn(MemberFixture.ID.toString());
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));

    //then
    assertThatThrownBy(() -> authService.createAccessToken(tokenRequest))
        .isInstanceOf(TokenNotValidException.class);
  }
  @DisplayName("?????? ????????? ?????? - RefreshToken ??? DB??? ????????? ????????? ?????? ????????? TokenNotValidException ??? ????????????. ")
  @Test
  public void throwExceptionIfRefreshTokenIsNotMatch(){
    //given, when
    TokenRequest tokenRequest = new TokenRequest(WRONG_REFRESH_TOKEN);

    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(true);
    given(jwtTokenProvider.getPayload(anyString()))
        .willReturn(MemberFixture.ID.toString());
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(refreshTokenRepository.findByMemberId(anyLong()))
        .willReturn(Optional.of(new RefreshToken(REFRESH_TOKEN,MemberFixture.ID)));

    //then
    assertThatThrownBy(() -> authService.createAccessToken(tokenRequest))
        .isInstanceOf(TokenNotValidException.class);
  }
  @DisplayName("?????? ????????? ?????? - RefreshToken ??? DB??? ???????????? ????????? TokenNotFoundException ??? ????????????. ")
  @Test
  public void throwExceptionIfRefreshTokenIsNotFound(){
    //given,when
    TokenRequest tokenRequest = new TokenRequest(REFRESH_TOKEN);

    given(jwtTokenProvider.validateToken(anyString()))
        .willReturn(true);
    given(jwtTokenProvider.getPayload(anyString()))
        .willReturn(MemberFixture.ID.toString());
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(refreshTokenRepository.findByMemberId(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> authService.createAccessToken(tokenRequest))
        .isInstanceOf(TokenNotFoundException.class);
  }

}
