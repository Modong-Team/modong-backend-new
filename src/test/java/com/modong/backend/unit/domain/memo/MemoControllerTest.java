package com.modong.backend.unit.domain.memo;

import static com.modong.backend.Fixtures.ApplicantFixture.APPLICANT_ID;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemoFixture.MEMO_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.modong.backend.Fixtures.MemberFixture;
import com.modong.backend.auth.member.Dto.MemberCheckRequest;
import com.modong.backend.domain.memo.MemoController;
import com.modong.backend.domain.memo.dto.MemoCreateRequest;
import com.modong.backend.unit.base.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(MemoController.class)
public class MemoControllerTest extends ControllerTest {

  private String requestBody;
  @BeforeEach
  public void init(){

  }
  //지금 Auth에서 인증정보 처리를 어떻게 테스트 하는지 몰라서 일단 보류 ㅇㅇ
  @DisplayName("메모 생성 성공")
  @WithMockUser
  @Test
  public void returnIdWithStatusCreated() throws Exception {
    // given
    Long expected = MEMO_ID;
    requestBody = objectMapper.writeValueAsString(memoCreateRequest);

    given(memoService.create(any(),any(),any())).willReturn(expected);
    given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
    given(jwtTokenProvider.getPayload(anyString())).willReturn(String.valueOf(MemberFixture.ID));

    // when
    ResultActions perform = mockMvc.perform(post("/api/v1/applicants/"+ String.valueOf(APPLICANT_ID) +"/memos")
        .header(AUTHORIZATION_HEADER_NAME,AUTHORIZATION_HEADER_VALUE)
        .contentType(MediaType.APPLICATION_JSON).with(csrf())
        .content(requestBody));

    // then
    perform
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("data.id").value(expected));
  }
}
