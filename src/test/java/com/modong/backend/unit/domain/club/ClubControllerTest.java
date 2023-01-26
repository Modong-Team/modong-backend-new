package com.modong.backend.unit.domain.club;

import static com.modong.backend.Fixtures.ClubFixture.CLUB_ID;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_NAME;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_PROFILE_IMG_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.ClubController;
import com.modong.backend.domain.club.Dto.ClubRequest;
import com.modong.backend.domain.club.Dto.ClubResponse;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import com.modong.backend.unit.base.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ClubController.class)
public class ClubControllerTest extends ControllerTest {
  private String requestBody;
  private ClubRequest clubRequest;
  @DisplayName("동아리 생성 성공 - 유효한 정보의 동아리 생성 요청이 오면 회원가입 페이지 주소와 함께 상태값 201을 반환해야 한다.")
  @WithMockUser
  @Test
  public void returnSavedIdWithStatusCREATEDIfClubRequestValid() throws Exception {
    // given
    Long response = CLUB_ID;

    clubRequest = new ClubRequest(CLUB_NAME,CLUB_PROFILE_IMG_URL);
    requestBody = objectMapper.writeValueAsString(clubRequest);

    given(clubService.save(any()))
        .willReturn(response);

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/club")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(redirectedUrl("/api/v1/register"))
        .andExpect(jsonPath("data.id").value(response));
  }

  @DisplayName("동아리 조회 성공 - 유요한 Id로 동아리을 조회한다.")
  @WithMockUser
  @Test
  public void returnClubIfIdIsValid() throws Exception {
    // given
    clubRequest = new ClubRequest(CLUB_NAME,CLUB_PROFILE_IMG_URL);
    ClubResponse clubResponse = new ClubResponse(new Club(clubRequest));

    given(clubService.findById(any()))
        .willReturn(clubResponse);

    // when
    ResultActions perform = mockMvc.perform(get(String.format("/api/v1/club/%d",anyLong())));

    // then
    perform
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.id").value(clubResponse.getId()))
        .andExpect(jsonPath("data.name").value(clubResponse.getName()))
        .andExpect(jsonPath("data.clubCode").value(clubResponse.getClubCode()))
        .andExpect(jsonPath("data.profileImgUrl").value(clubResponse.getProfileImgUrl()));
  }
  @DisplayName("동아리 조회 실패 - 요청 id로 동아리을 찾을 수 없는 경우 가 발생된다..")
  @WithMockUser
  @Test
  public void throwExceptionIfIdNotExist() throws Exception {
    // given

    ClubNotFoundException expected = new ClubNotFoundException(CLUB_ID);

    given(clubService.findById(any()))
        .willThrow(expected);

    // when
    ResultActions perform = mockMvc.perform(get(String.format("/api/v1/club/%d",anyLong())));

    // then
    perform
        .andDo(print())
        .andExpect(status().isNotFound());
  }
}