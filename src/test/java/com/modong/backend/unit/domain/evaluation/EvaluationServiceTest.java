package com.modong.backend.unit.domain.evaluation;

import static com.modong.backend.Fixtures.ApplicantFixture.APPLICANT_ID;
import static com.modong.backend.Fixtures.ApplicantFixture.APPLICANT_RATE;
import static com.modong.backend.Fixtures.ApplicationFixture.APPLICATION_ID;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_ID;
import static com.modong.backend.Fixtures.EvaluationFixture.EVALUATION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.modong.backend.Fixtures.MemberFixture;
import com.modong.backend.auth.member.Member;
import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.application.Application;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.clubMemeber.ClubMember;
import com.modong.backend.domain.evaluation.Evaluation;
import com.modong.backend.domain.evaluation.EvaluationService;
import com.modong.backend.domain.evaluation.dto.EvaluationResponse;
import com.modong.backend.global.exception.applicant.ApplicantNotFoundException;
import com.modong.backend.global.exception.application.ApplicationNotFoundException;
import com.modong.backend.global.exception.auth.NoPermissionCreateException;
import com.modong.backend.global.exception.auth.NoPermissionDeleteException;
import com.modong.backend.global.exception.auth.NoPermissionReadException;
import com.modong.backend.global.exception.auth.NoPermissionUpdateException;
import com.modong.backend.global.exception.evaluation.AlreadyExistsException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import com.modong.backend.unit.base.ServiceTest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

public class EvaluationServiceTest extends ServiceTest {

  @Autowired
  private EvaluationService evaluationService;

  private Evaluation evaluation;

  private Club club,another;
  private Application application;
  private Applicant applicant;
  private Member member;

  @BeforeEach
  public void init(){

    club = new Club(clubCreateRequest);
    another = new Club(clubCreateRequest);

    member = new Member(memberRegisterRequest);

    ReflectionTestUtils.setField(member,"id", MemberFixture.ID);

    application = new Application(applicationCreateRequest,club);

    applicant = new Applicant(applicantCreateRequest,application);

    ReflectionTestUtils.setField(club,"id", CLUB_ID);
    ReflectionTestUtils.setField(another,"id", CLUB_ID + 1L);
    ReflectionTestUtils.setField(application,"id", APPLICATION_ID);
    ReflectionTestUtils.setField(applicant,"id", APPLICANT_ID);

    evaluation = new Evaluation(evaluationCreateRequest_userA,member,applicant);

    ReflectionTestUtils.setField(evaluation,"id", EVALUATION_ID);

  }

  @DisplayName("평가 생성 성공")
  @Test
  public void SuccessCreateEvaluation(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(application));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));
    given(evaluationRepository.existsByApplicantIdAndMemberId(anyLong(),anyLong())).willReturn(false);
    given(evaluationRepository.save(any())).willReturn(evaluation);
    given(applicantRepositoryCustom.getRateByApplicantId(anyLong())).willReturn(APPLICANT_RATE);
    //생성 권한 주기
    member.addClub(new ClubMember(club,member));


    //when
    Long savedId = evaluationService.create(evaluationCreateRequest_userA,MemberFixture.ID);

    //then
    assertThatCode(() -> evaluationService.create(evaluationCreateRequest_userA,MemberFixture.ID)).doesNotThrowAnyException();

    assertThat(savedId).isEqualTo(EVALUATION_ID);
  }
  @DisplayName("평가 생성 실패 - 회원 조회 실패")
  @Test
  public void FailCreateEvaluation_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(application));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest_userA,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("평가 생성 실패 - 지원서 조회 실패")
  @Test
  public void FailCreateEvaluation_ApplicationNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest_userA,MemberFixture.ID))
        .isInstanceOf(ApplicationNotFoundException.class);
  }
  @DisplayName("평가 생성 실패 - 지원자 조회 실패")
  @Test
  public void FailCreateEvaluation_ApplicantNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(application));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest_userA,MemberFixture.ID))
        .isInstanceOf(ApplicantNotFoundException.class);
  }
  @DisplayName("평가 생성 실패 - 이미 이전에 한 평가가 존재함")
  @Test
  public void FailCreateEvaluation_AlreadyExists(){
    //given, when
    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(application));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));
    given(evaluationRepository.existsByApplicantIdAndMemberId(anyLong(),anyLong())).willReturn(true);

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest_userA,MemberFixture.ID))
        .isInstanceOf(AlreadyExistsException.class);
  }


  @DisplayName("평가 생성 실패 - 권한 없음(회원의 동아리와 지원서를 수정할 수 있는 동아리가 다를 경우)")
  @Test
  public void FailCreateEvaluation_UnAuthorized(){
    //given, when
    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(application));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //생성 권한 없는 경우
    member.addClub(new ClubMember(another,member));

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest_userA,MemberFixture.ID))
        .isInstanceOf(NoPermissionCreateException.class);
  }

  @DisplayName("평가 수정 성공")
  @Test
  public void SuccessUpdateEvaluation(){
    //given

    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));
    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(evaluationRepository.save(any())).willReturn(evaluation);
    given(applicantRepositoryCustom.getRateByApplicantId(anyLong())).willReturn(APPLICANT_RATE);

    //when
    Long savedId = evaluationService.update(evaluationUpdateRequest,MemberFixture.ID);

    //then
    assertThatCode(() -> evaluationService.update(evaluationUpdateRequest,MemberFixture.ID)).doesNotThrowAnyException();

    assertThat(savedId).isEqualTo(EVALUATION_ID);
  }
  @DisplayName("평가 수정 실패 - 회원 조회 실패")
  @Test
  public void FailUpdateEvaluation_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));

    //then
    assertThatThrownBy(() -> evaluationService.update(evaluationUpdateRequest,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("평가 수정 실패 - 권한 없음(회원의 동아리와 지원서를 수정할 수 있는 동아리가 다를 경우)")
  @Test
  public void FailUpdateEvaluation_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));
    //수정 권한 없게 설정
    ReflectionTestUtils.setField(evaluation,"creatorId", member.getId()+1L);
    //then
    assertThatThrownBy(() -> evaluationService.update(evaluationUpdateRequest,MemberFixture.ID))
        .isInstanceOf(NoPermissionUpdateException.class);
  }
  @DisplayName("평가 삭제 성공")
  @Test
  public void SuccessDeleteEvaluation(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));
    given(evaluationRepository.save(any())).willReturn(evaluation);
    given(applicantRepositoryCustom.getRateByApplicantId(anyLong())).willReturn(APPLICANT_RATE);

    //when
    evaluationService.delete(evaluationDeleteRequest,MemberFixture.ID);

    //then
    assertThatCode(() -> evaluationService.delete(evaluationDeleteRequest,MemberFixture.ID)).doesNotThrowAnyException();

  }
  @DisplayName("평가 삭제 실패 - 회원 조회 실패")
  @Test
  public void FailDeleteEvaluation_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));

    //then
    assertThatThrownBy(() -> evaluationService.delete(evaluationDeleteRequest,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("평가 삭제 실패 - 권한 없음(회원의 동아리와 지원서를 수정할 수 있는 동아리가 다를 경우)")
  @Test
  public void FailDeleteEvaluation_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));

    //삭제 권한 없게 설정
    ReflectionTestUtils.setField(evaluation,"creatorId", member.getId()+1L);
    //then
    assertThatThrownBy(() -> evaluationService.delete(evaluationDeleteRequest,MemberFixture.ID))
        .isInstanceOf(NoPermissionDeleteException.class);
  }
  @DisplayName("지원자에 대한 모든 평가 조회 성공")
  @Test
  public void SuccessFindEvaluations_ApplicationID(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(application));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));
    given(evaluationRepository.findAllByApplicantIdAndIsDeletedIsFalse(anyLong())).willReturn(Arrays.asList(evaluation));

    //조회 권한 주기
    member.addClub(new ClubMember(club,member));

    List<EvaluationResponse> expected = Arrays.asList(new EvaluationResponse(evaluation,member, APPLICATION_ID, APPLICANT_ID));
    //when
    List<EvaluationResponse> actual = evaluationService.findAllByApplication(evaluationFindRequest,MemberFixture.ID);

    //then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    assertThatCode(() -> evaluationService.findAllByApplication(evaluationFindRequest,MemberFixture.ID)).doesNotThrowAnyException();
  }
  @DisplayName("지원자에 대한 모든 평가 조회 실패 - 회원 조회 실패")
  @Test
  public void FailFindEvaluations_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(application));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //then
    assertThatThrownBy(() -> evaluationService.findAllByApplication(evaluationFindRequest,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("지원자에 대한 모든 평가 조회 실패 - 지원서 조회 실패")
  @Test
  public void FailFindEvaluations_ApplicationNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //then
    assertThatThrownBy(() -> evaluationService.findAllByApplication(evaluationFindRequest,MemberFixture.ID))
        .isInstanceOf(ApplicationNotFoundException.class);
  }
  @DisplayName("지원자에 대한 모든 평가 조회 실패 - 권한 없음(회원의 동아리와 지원서를 수정할 수 있는 동아리가 다를 경우)")
  @Test
  public void FailFindEvaluations_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(application));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //조회 권한 없게 설정
    member.addClub(new ClubMember(another,member));
    //then
    assertThatThrownBy(() -> evaluationService.findAllByApplication(evaluationFindRequest,MemberFixture.ID))
        .isInstanceOf(NoPermissionReadException.class);
  }

}
