package com.modong.backend.unit.base;

import static com.modong.backend.Fixtures.ApplicantFixture.APPLICANT_ID;
import static com.modong.backend.Fixtures.ApplicantFixture.APPLICANT_NAME;
import static com.modong.backend.Fixtures.ApplicationFixture.APPLICATION_ID;
import static com.modong.backend.Fixtures.ApplicationFixture.E_QUESTION_ID_LIST;
import static com.modong.backend.Fixtures.ApplicationFixture.TITLE;
import static com.modong.backend.Fixtures.ApplicationFixture.UPDATE_E_QUESTION_ID_LIST;
import static com.modong.backend.Fixtures.ApplicationFixture.UPDATE_TITLE;
import static com.modong.backend.Fixtures.ApplicationFixture.URL_ID;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_CODE;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_END_DATE;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_ID;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_NAME;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_PROFILE_IMG_URL;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_START_DATE;
import static com.modong.backend.Fixtures.EssentialAnswerFixture.ESSENTIAL_ANSWER;
import static com.modong.backend.Fixtures.EssentialQuestionFixture.ESSENTIAL_QUESTION_ID;
import static com.modong.backend.Fixtures.EvaluationFixture.COMMENT;
import static com.modong.backend.Fixtures.EvaluationFixture.SCORE;
import static com.modong.backend.Fixtures.EvaluationFixture.UPDATE_COMMENT;
import static com.modong.backend.Fixtures.MemberFixture.EMAIL;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_A_EMAIL;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_A_MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_A_NAME;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_A_PHONE;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_B_EMAIL;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_B_MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_B_NAME;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_B_PHONE;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_C_EMAIL;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_C_MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_C_NAME;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_C_PHONE;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.NAME;
import static com.modong.backend.Fixtures.MemberFixture.PASSWORD;
import static com.modong.backend.Fixtures.MemberFixture.PHONE;
import static com.modong.backend.Fixtures.MemoFixture.MEMO_CONTENT;
import static com.modong.backend.Fixtures.MemoFixture.MEMO_UPDATED_CONTENT;
import static com.modong.backend.Fixtures.QuestionAnswerFixture.QUESTION_ANSWER;
import static com.modong.backend.Fixtures.QuestionFixture.QUESTION_ID;

import com.modong.backend.auth.member.Dto.MemberRegisterRequest;
import com.modong.backend.auth.JwtTokenProvider;
import com.modong.backend.auth.member.MemberRepository;
import com.modong.backend.auth.refreshToken.RefreshTokenRepository;
import com.modong.backend.domain.applicant.Dto.ApplicantCreateRequest;
import com.modong.backend.domain.applicant.repository.ApplicantRepository;
import com.modong.backend.domain.applicant.repository.ApplicantRepositoryCustomImpl;
import com.modong.backend.domain.application.ApplicationRepository;
import com.modong.backend.domain.application.Dto.ApplicationCreateRequest;
import com.modong.backend.domain.application.Dto.ApplicationUpdateRequest;
import com.modong.backend.domain.club.ClubRepository;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.domain.essentialAnswer.Dto.EssentialAnswerRequest;
import com.modong.backend.domain.essentialQuestion.EssentialQuestionRepository;
import com.modong.backend.domain.evaluation.EvaluationRepository;
import com.modong.backend.domain.evaluation.dto.EvaluationCreateRequest;
import com.modong.backend.domain.evaluation.dto.EvaluationUpdateRequest;
import com.modong.backend.domain.memo.MemoRepository;
import com.modong.backend.domain.memo.dto.MemoCreateRequest;
import com.modong.backend.domain.memo.dto.MemoUpdateRequest;
import com.modong.backend.domain.questionAnswer.Dto.QuestionAnswerRequest;

import java.util.Arrays;
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

  @MockBean
  protected ApplicantRepository applicantRepository;

  @MockBean
  protected ApplicantRepositoryCustomImpl applicantRepositoryCustom;

  @MockBean
  protected MemoRepository memoRepository;

  @MockBean
  protected EvaluationRepository evaluationRepository;

  @Autowired
  protected PasswordEncoder passwordEncoder;

  @MockBean
  protected JwtTokenProvider jwtTokenProvider;
  protected MemberRegisterRequest memberRegisterRequest = MemberRegisterRequest.builder()
      .memberId(MEMBER_ID).email(EMAIL).password(PASSWORD)
      .phone(PHONE).name(NAME).clubCode(CLUB_CODE).build();

  protected MemberRegisterRequest memberRegisterRequest_userA = MemberRegisterRequest.builder()
      .memberId(MEMBER_A_MEMBER_ID).email(MEMBER_A_EMAIL).password(PASSWORD)
      .phone(MEMBER_A_PHONE).name(MEMBER_A_NAME).clubCode(CLUB_CODE).build();

  protected MemberRegisterRequest memberRegisterRequest_userB = MemberRegisterRequest.builder()
      .memberId(MEMBER_B_MEMBER_ID).email(MEMBER_B_EMAIL).password(PASSWORD)
      .phone(MEMBER_B_PHONE).name(MEMBER_B_NAME).clubCode(CLUB_CODE).build();

  protected MemberRegisterRequest memberRegisterRequest_userC = MemberRegisterRequest.builder()
      .memberId(MEMBER_C_MEMBER_ID).email(MEMBER_C_EMAIL).password(PASSWORD)
      .phone(MEMBER_C_PHONE).name(MEMBER_C_NAME).clubCode(CLUB_CODE).build();

  protected ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
      .name(CLUB_NAME).profileImgUrl(CLUB_PROFILE_IMG_URL)
      .startDate(CLUB_START_DATE).endDate(CLUB_END_DATE).build();
  protected ApplicationCreateRequest applicationCreateRequest = ApplicationCreateRequest.builder()
      .title(TITLE).essentialQuestionIds(E_QUESTION_ID_LIST).urlId(URL_ID).build();
  protected ApplicationUpdateRequest applicationUpdateRequest = ApplicationUpdateRequest.builder()
      .title(UPDATE_TITLE).essentialQuestionIds(UPDATE_E_QUESTION_ID_LIST).build();

  protected EssentialAnswerRequest essentialAnswerRequest = EssentialAnswerRequest.builder()
      .essentialQuestionId(ESSENTIAL_QUESTION_ID)
      .answer(ESSENTIAL_ANSWER).build();

  protected QuestionAnswerRequest questionAnswerRequest = QuestionAnswerRequest.builder()
      .questionId(QUESTION_ID)
      .answer(QUESTION_ANSWER).build();
  protected ApplicantCreateRequest applicantCreateRequest = ApplicantCreateRequest.builder()
      .applicationId(APPLICATION_ID).name(APPLICANT_NAME).essentialAnswers(Arrays.asList(essentialAnswerRequest))
      .questionAnswers(Arrays.asList(questionAnswerRequest)).build();

  protected MemoCreateRequest memoCreateRequest = MemoCreateRequest.builder()
      .applicantId(APPLICANT_ID)
      .content(MEMO_CONTENT)
      .build();

  protected MemoUpdateRequest memoUpdateRequest = MemoUpdateRequest.builder()
      .content(MEMO_UPDATED_CONTENT)
      .build();

  protected EvaluationCreateRequest evaluationCreateRequest = EvaluationCreateRequest.builder()
      .comment(COMMENT)
      .score(SCORE)
      .build();

  protected EvaluationUpdateRequest evaluationUpdateRequest = EvaluationUpdateRequest.builder()
      .newComment(UPDATE_COMMENT)
      .newScore(SCORE + 1.0f)
      .build();
}
