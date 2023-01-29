package com.modong.backend.unit.domain.member;

import static com.modong.backend.Fixtures.ClubFixture.CLUB_CODE;
import static com.modong.backend.Fixtures.MemberFixture.EMAIL;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.NAME;
import static com.modong.backend.Fixtures.MemberFixture.PASSWORD;
import static com.modong.backend.Fixtures.MemberFixture.PHONE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.modong.backend.Fixtures.MemberFixture;
import com.modong.backend.auth.member.Dto.MemberCheckRequest;
import com.modong.backend.auth.member.Dto.MemberRegisterRequest;
import com.modong.backend.auth.member.MemberController;
import com.modong.backend.global.exception.WrongFormatException;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import com.modong.backend.global.exception.member.DuplicateMemberIdException;
import com.modong.backend.unit.base.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MemberController.class)
public class MemberControllerTest extends ControllerTest {

  private String requestBody;

  private MemberRegisterRequest memberRegisterRequest;

  @BeforeEach
  public void init(){
    memberRegisterRequest = MemberRegisterRequest.builder()
        .memberId(MEMBER_ID).email(EMAIL).password(PASSWORD)
        .phone(PHONE).name(NAME).clubCode(CLUB_CODE).build();
  }
  @DisplayName("회원 ID 중복검사 성공 - DB에 없는 회원 ID를 사용할 경우 true 를 반환한다.")
  @WithMockUser
  @Test
  public void returnTrueWithStatusOKIfMemberIDNotExist() throws Exception {
    // given
    MemberCheckRequest memberCheckRequest = new MemberCheckRequest(MEMBER_ID);
    Boolean result = true;
    requestBody = objectMapper.writeValueAsString(memberCheckRequest);

    given(memberService.checkMemberId(any())).willReturn(result);

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/member/check")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.result").value(result));
  }
  @DisplayName("회원 ID 중복검사 실패 - DB에 있는 회원 ID를 요청으로 보낸 경우  를 반환한다.")
  @WithMockUser
  @Test
  public void throwDuplicateMemberIdExceptionIfClubNotExist() throws Exception {
    // given
    MemberCheckRequest memberCheckRequest = new MemberCheckRequest(MEMBER_ID);
    requestBody = objectMapper.writeValueAsString(memberCheckRequest);
    DuplicateMemberIdException expected = new DuplicateMemberIdException();

    given(memberService.checkMemberId(any())).willThrow(expected);

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/member/check")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform.andExpect(status().isBadRequest());
  }

  @DisplayName("회원가입 성공 - 유효한 동아리 코드와 알맞은 회원 데이터로 회원가입 요청이 오면 DB에 저장하고 201을 반환해야 한다.")
  @WithMockUser
  @Test
  public void returnIdWithStatusCreatedIfRegisterRequestValid() throws Exception {
    // given
    Long response = MemberFixture.ID;
    requestBody = objectMapper.writeValueAsString(memberRegisterRequest);

    given(memberService.register(any()))
        .willReturn(response);

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/register")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(redirectedUrl("/api/v1/member/" + response))
        .andExpect(jsonPath("data.id").value(response));
  }
  @DisplayName("회원가입 실패 - 동아리 코드를 가진 동아리가 존재하지 않으면 상태값 404를 반환해야 한다.")
  @WithMockUser
  @Test
  public void throwNotFoundExceptionIfClubNotExist() throws Exception{
    //given
    requestBody = objectMapper.writeValueAsString(memberRegisterRequest);

    ClubNotFoundException expected = new ClubNotFoundException(memberRegisterRequest.getClubCode());
    given(memberService.register(any()))
        .willThrow(expected);
    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/register").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody));
    //then
    perform.andExpect(status().isNotFound());
  }

  @DisplayName("회원가입 실패 - 회원가입 요청에 담긴 데이터가 유효 않으면 상태값 400를 반환해야 한다.")
  @WithMockUser
  @Test
  public void throwBadRequestExceptionIfRegisterRequestNotValid() throws Exception{
    //given
    requestBody = objectMapper.writeValueAsString(memberRegisterRequest);

    WrongFormatException expected = new WrongFormatException();
    given(memberService.register(any()))
        .willThrow(expected);
    //when
    ResultActions perform = mockMvc.perform(post("/api/v1/register").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody));
    //then
    perform.andExpect(status().isBadRequest());
  }

}
