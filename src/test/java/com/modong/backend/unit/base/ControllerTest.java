package com.modong.backend.unit.base;

import static com.modong.backend.Fixtures.ApplicantFixture.APPLICANT_ID;
import static com.modong.backend.Fixtures.ApplicationFixture.APPLICATION_ID;
import static com.modong.backend.Fixtures.MemoFixture.MEMO_CONTENT;
import static com.modong.backend.Fixtures.MemoFixture.MEMO_ID;
import static com.modong.backend.Fixtures.MemoFixture.MEMO_UPDATED_CONTENT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modong.backend.auth.AuthService;
import com.modong.backend.auth.JwtTokenProvider;
import com.modong.backend.auth.member.MemberService;
import com.modong.backend.config.AuthConfig;
import com.modong.backend.domain.club.ClubService;
import com.modong.backend.domain.memo.MemoService;
import com.modong.backend.domain.memo.dto.MemoCreateRequest;
import com.modong.backend.domain.memo.dto.MemoDeleteRequest;
import com.modong.backend.domain.memo.dto.MemoFindRequest;
import com.modong.backend.domain.memo.dto.MemoUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@MockBean({JwtTokenProvider.class,AuthConfig.class})
public class ControllerTest {

  @MockBean
  protected MemberService memberService;

  @MockBean
  protected ClubService clubService;

  @MockBean
  protected AuthService authService;

  @MockBean
  protected MemoService memoService;

  @MockBean
  protected JwtTokenProvider jwtTokenProvider;
  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  protected static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  protected static final String AUTHORIZATION_HEADER_VALUE = "Bearer aaaaaaaa.bbbbbbbb.cccccccc";
  protected MemoCreateRequest memoCreateRequest = MemoCreateRequest.builder()
      .applicationId(APPLICATION_ID)
      .applicantId(APPLICANT_ID)
      .content(MEMO_CONTENT)
      .build();

  protected MemoUpdateRequest memoUpdateRequest = MemoUpdateRequest.builder()
      .memoId(MEMO_ID)
      .content(MEMO_UPDATED_CONTENT)
      .build();
  protected MemoDeleteRequest memoDeleteRequest = MemoDeleteRequest.builder()
      .memoId(MEMO_ID)
      .build();

  protected MemoFindRequest memoFindRequest = MemoFindRequest.builder()
      .applicantId(APPLICANT_ID)
      .applicationId(APPLICATION_ID)
      .build();

}
