package com.modong.backend.unit.domain.club;

import static com.modong.backend.Fixtures.ClubFixture.CLUB_CODE;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_END_DATE;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_ID;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_NAME;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_PROFILE_IMG_URL;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_START_DATE;
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
import com.modong.backend.domain.club.Dto.ClubCheckRequest;
import com.modong.backend.domain.club.ClubController;
import com.modong.backend.domain.club.Dto.ClubCreateResponse;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.domain.club.Dto.ClubResponse;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import com.modong.backend.unit.base.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ClubController.class)
public class ClubControllerTest extends ControllerTest {
  private String requestBody;
  private ClubCreateRequest clubCreateRequest;
  @DisplayName("????????? ?????? ?????? - ????????? ????????? ????????? ?????? ????????? ?????? ???????????? ????????? ????????? ?????? ????????? 201??? ???????????? ??????.")
  @WithMockUser
  @Test
  public void returnSavedIdWithStatusCREATEDIfClubRequestValid() throws Exception {
    // given

    final ClubCreateRequest clubCreateRequest = new ClubCreateRequest(CLUB_NAME,CLUB_PROFILE_IMG_URL,CLUB_START_DATE,CLUB_END_DATE);
    final Club club = new Club(clubCreateRequest);
    ReflectionTestUtils.setField(club,"id",CLUB_ID);

    requestBody = objectMapper.writeValueAsString(clubCreateRequest);

    given(clubService.save(any()))
        .willReturn(new ClubCreateResponse(club));

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/club")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(redirectedUrl("/api/v1/register"))
        .andExpect(jsonPath("data.id").value(CLUB_ID))
        .andExpect(jsonPath("data.code").isNotEmpty());
  }

  @DisplayName("????????? ?????? ?????? - ????????? Id??? ???????????? ????????????.")
  @WithMockUser
  @Test
  public void returnClubIfIdIsValid() throws Exception {
    // given
    final ClubCreateRequest clubCreateRequest = new ClubCreateRequest(CLUB_NAME,CLUB_PROFILE_IMG_URL,CLUB_START_DATE,CLUB_END_DATE);
    ClubResponse clubResponse = new ClubResponse(new Club(clubCreateRequest));

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
  @DisplayName("????????? ?????? ?????? - ?????? id??? ???????????? ?????? ??? ?????? ?????? ??? ????????????..")
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
        .andExpect(status().isBadRequest());
  }

  @DisplayName("????????? ?????? ???????????? ?????? - DB??? ?????? ????????? ????????? ????????? ?????? false ??? ???????????? ??????.")
  @WithMockUser
  @Test
  public void returnFalseWithStatusOKIfClubNotExist() throws Exception {
    // given
    ClubCheckRequest clubCheckRequest = new ClubCheckRequest(CLUB_CODE);
    boolean result = false;
    requestBody = objectMapper.writeValueAsString(clubCheckRequest);

    given(clubService.checkClubCode(any())).willReturn(result);

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/club/check")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.exists").value(result));
  }

  @DisplayName("????????? ?????? ???????????? ?????? - DB??? ?????? ????????? ????????? ????????? ?????? true ??? ???????????? ??????.")
  @WithMockUser
  @Test
  public void returnTrueWithStatusOKIfClubExist() throws Exception {
    // given
    ClubCheckRequest clubCheckRequest = new ClubCheckRequest(CLUB_CODE);
    boolean result = true;
    requestBody = objectMapper.writeValueAsString(clubCheckRequest);

    given(clubService.checkClubCode(any())).willReturn(result);

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/club/check")
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("data.exists").value(result));
  }
}
