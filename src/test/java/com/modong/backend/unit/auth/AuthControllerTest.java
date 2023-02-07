package com.modong.backend.unit.auth;
import static com.modong.backend.Fixtures.AuthFixture.ACCESS_TOKEN;
import static com.modong.backend.Fixtures.AuthFixture.NEW_ACCESS_TOKEN;
import static com.modong.backend.Fixtures.AuthFixture.NEW_REFRESH_TOKEN;
import static com.modong.backend.Fixtures.AuthFixture.REFRESH_TOKEN;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.PASSWORD;
import static com.modong.backend.Fixtures.MemberFixture.WRONG_MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.WRONG_PASSWORD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.modong.backend.Fixtures.MemberFixture;
import com.modong.backend.auth.AuthController;
import com.modong.backend.auth.Dto.LoginRequest;
import com.modong.backend.auth.Dto.TokenRequest;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.global.exception.WrongFormatException;
import com.modong.backend.global.exception.auth.PasswordMismatchException;
import com.modong.backend.global.exception.auth.RefreshTokenNotValidException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import com.modong.backend.unit.base.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(AuthController.class)
public class AuthControllerTest extends ControllerTest {

  private String requestBody;
  private LoginRequest loginRequest;
  @DisplayName("로그인 성공 - 유효한 정보의 로그인 요청이 오면 토큰을 발급하고 상태값 200을 반환해야 한다.")
  @WithMockUser
  @Test
  public void returnTokenWithStatusOKIfLoginRequestValid() throws Exception {
    // given
    loginRequest = new LoginRequest(MEMBER_ID,PASSWORD);
    requestBody = objectMapper.writeValueAsString(loginRequest);

    given(authService.login(any()))
        .willReturn(new TokenResponse(MemberFixture.ID,ACCESS_TOKEN,REFRESH_TOKEN));

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.accessToken").value(ACCESS_TOKEN))
        .andExpect(jsonPath("data.refreshToken").value(REFRESH_TOKEN));
  }
  @DisplayName("로그인 실패 - MemberId가 존재하지 않으면 상태값 404를 반환해야 한다.")
  @WithMockUser
  @Test
  public void throwNotFoundExceptionIfMemberIdNotExist() throws Exception{
    //given
    loginRequest = new LoginRequest(MEMBER_ID,PASSWORD);
    requestBody = objectMapper.writeValueAsString(loginRequest);

    MemberNotFoundException expected = new MemberNotFoundException(loginRequest.getMemberId());
    given(authService.login(any()))
        .willThrow(expected);
    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));
    //then
    perform.andExpect(status().isNotFound());
  }
  @DisplayName("로그인 실패 - 비밀번호가 틀리면 상태값 400를 반환해야 한다.")
  @WithMockUser
  @Test
  public void throwExceptionIfPasswordNotMatch() throws Exception{
    //given

    loginRequest = new LoginRequest(MEMBER_ID,PASSWORD);
    requestBody = objectMapper.writeValueAsString(loginRequest);

    PasswordMismatchException expected = new PasswordMismatchException();
    given(authService.login(any()))
        .willThrow(expected);
    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));
    //then
    perform.andExpect(status().isBadRequest());
  }

  @DisplayName("로그인 실패 - 아이디 형식이 잘못된 경우")
  @WithMockUser
  @Test
  public void throwExceptionIfMemberIdFormatIsWrong() throws Exception{
    //given
    loginRequest = new LoginRequest(WRONG_MEMBER_ID,PASSWORD);
    requestBody = objectMapper.writeValueAsString(loginRequest);

    WrongFormatException expected = new WrongFormatException();
    given(authService.login(any()))
        .willThrow(expected);
    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));
    //then
    perform.andExpect(status().isBadRequest());
  }

  @DisplayName("로그인 실패 - 비밀번호 형식이 잘못된 경우")
  @WithMockUser
  @Test
  public void throwExceptionIfPasswordFormatIsWrong() throws Exception{
    //given
    loginRequest = new LoginRequest(MEMBER_ID,WRONG_PASSWORD);
    requestBody = objectMapper.writeValueAsString(loginRequest);

    WrongFormatException expected = new WrongFormatException();
    given(authService.login(any()))
        .willThrow(expected);
    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/login")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));
    //then
    perform.andExpect(status().isBadRequest());
  }

  // 토큰 재발행
  @DisplayName("토큰 재발행 성공")
  @WithMockUser
  @Test
  public void returnTokenWithStatusOKIfTokenRequestValid() throws Exception{
    //given
    TokenRequest tokenRequest = new TokenRequest(REFRESH_TOKEN);

    requestBody = objectMapper.writeValueAsString(tokenRequest);

    given(authService.createAccessToken(any()))
        .willReturn(new TokenResponse(MemberFixture.ID,NEW_ACCESS_TOKEN,NEW_REFRESH_TOKEN));

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/token")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.accessToken").value(NEW_ACCESS_TOKEN))
        .andExpect(jsonPath("data.refreshToken").value(NEW_REFRESH_TOKEN));
  }

  @DisplayName("토큰 재발행 실패 - MemberId가 존재하지 않으면 상태값 404를 반환해야 한다.")
  @WithMockUser
  @Test
  public void throwExceptionIfMemberIdNotFound() throws Exception{
    //given
    TokenRequest tokenRequest = new TokenRequest(REFRESH_TOKEN);

    requestBody = objectMapper.writeValueAsString(tokenRequest);

    MemberNotFoundException expected = new MemberNotFoundException(MemberFixture.ID);

    given(authService.createAccessToken(any()))
        .willThrow(expected);
    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/token")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));
    //then
    perform.andExpect(status().isNotFound());
  }

  @DisplayName("토큰 재발행 실패 - 리프레시 토큰이 유효하지 않으면 상태값 401를 반환해야 한다.")
  @WithMockUser
  @Test
  public void throwExceptionIfRefreshTokenNotValid() throws Exception{
    //given
    TokenRequest tokenRequest = new TokenRequest(REFRESH_TOKEN);

    requestBody = objectMapper.writeValueAsString(tokenRequest);

    RefreshTokenNotValidException expected = new RefreshTokenNotValidException();

    given(authService.createAccessToken(any()))
        .willThrow(expected);
    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/token")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));
    //then
    perform.andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
  }
}
