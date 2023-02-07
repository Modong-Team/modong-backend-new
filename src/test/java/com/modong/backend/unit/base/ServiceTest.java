package com.modong.backend.unit.base;

import static com.modong.backend.Fixtures.ApplicationFixture.E_QUESTION_ID_LIST;
import static com.modong.backend.Fixtures.ApplicationFixture.TITLE;
import static com.modong.backend.Fixtures.ApplicationFixture.UPDATE_E_QUESTION_ID_LIST;
import static com.modong.backend.Fixtures.ApplicationFixture.UPDATE_TITLE;
import static com.modong.backend.Fixtures.ApplicationFixture.URL_ID;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_CODE;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_ID;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_NAME;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_PROFILE_IMG_URL;
import static com.modong.backend.Fixtures.MemberFixture.EMAIL;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.NAME;
import static com.modong.backend.Fixtures.MemberFixture.PASSWORD;
import static com.modong.backend.Fixtures.MemberFixture.PHONE;

import com.modong.backend.Fixtures.ApplicationFixture;
import com.modong.backend.auth.member.Dto.MemberRegisterRequest;
import com.modong.backend.auth.JwtTokenProvider;
import com.modong.backend.auth.member.MemberRepository;
import com.modong.backend.auth.refreshToken.RefreshTokenRepository;
import com.modong.backend.domain.application.ApplicationRepository;
import com.modong.backend.domain.application.Dto.ApplicationCreateRequest;
import com.modong.backend.domain.application.Dto.ApplicationUpdateRequest;
import com.modong.backend.domain.club.ClubRepository;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
import com.modong.backend.domain.essentialQuestion.EssentialQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


// 추후에 이거 SpringBootTest 대신에 아래 사항 참고해서 Mock 객체로 테스트 할 수 있도록 변경하기 !
// https://cornswrold.tistory.com/369
// https://myborn.tistory.com/16
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ServiceTest {
  @MockBean
  protected MemberRepository memberRepository;

  @MockBean
  protected ClubRepository clubRepository;

  @MockBean
  protected RefreshTokenRepository refreshTokenRepository;

  @MockBean
  protected EssentialQuestionRepository essentialQuestionRepository;

  @MockBean
  protected ApplicationRepository applicationRepository;

  @Autowired
  protected PasswordEncoder passwordEncoder;

  @MockBean
  protected JwtTokenProvider jwtTokenProvider;
  protected MemberRegisterRequest memberRegisterRequest = MemberRegisterRequest.builder()
      .memberId(MEMBER_ID).email(EMAIL).password(PASSWORD)
      .phone(PHONE).name(NAME).clubCode(CLUB_CODE).build();
  
  protected ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
      .name(CLUB_NAME).profileImgUrl(CLUB_PROFILE_IMG_URL).build();
  protected ApplicationCreateRequest applicationCreateRequest = ApplicationCreateRequest.builder()
      .title(TITLE).clubId(CLUB_ID).essentialQuestionIds(E_QUESTION_ID_LIST).urlId(URL_ID).build();
  protected ApplicationUpdateRequest applicationUpdateRequest = ApplicationUpdateRequest.builder()
      .title(UPDATE_TITLE).essentialQuestionIds(UPDATE_E_QUESTION_ID_LIST).build();
}
