package com.modong.backend.integration.api;

import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.PASSWORD;

import com.modong.backend.auth.Dto.LoginRequest;
import com.modong.backend.auth.Dto.TokenRequest;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.domain.club.Dto.ClubCreateResponse;
import com.modong.backend.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.assertj.core.api.Assertions.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class AuthIntegrationTest extends IntegrationTest {

  private String requestBody;

  private Long memberId;
  private TokenResponse tokenResponse;
  private ClubCreateResponse clubResponse;

  @BeforeEach
  public void setUp(){
    clubResponse = clubSetUp();
    memberId = memberSetUp(clubResponse.getCode());
    tokenResponse = tokenSetUp(memberId);
  }

  @DisplayName("로그인 성공")
  @Test
  public void LoginSuccess() throws Exception {
    //given
    final LoginRequest loginRequest = new LoginRequest(MEMBER_ID,PASSWORD);
    requestBody = objectMapper.writeValueAsString(loginRequest);

    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    //then
    perform
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.accessToken").isNotEmpty())
        .andExpect(jsonPath("data.refreshToken").isNotEmpty());
  }
  @DisplayName("토큰 재발행 성공")
  @Test
  public void tokenReissueSuccess() throws Exception {

    //given
    final TokenRequest tokenRequest = new TokenRequest(tokenResponse.getRefreshToken());

    requestBody = objectMapper.writeValueAsString(tokenRequest);

    //when
    ResultActions performLogin = mockMvc.perform(post("/api/v1/token")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    //then tokenRe
    performLogin
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
        .andExpect(jsonPath("$[?(@.refreshToken != '%s')]",tokenResponse.getRefreshToken()).isNotEmpty());
  }
}
