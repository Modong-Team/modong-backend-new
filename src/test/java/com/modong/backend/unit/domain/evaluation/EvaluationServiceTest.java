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
import org.junit.platform.commons.util.ReflectionUtils;
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

    member = new Member(memberRegisterRequest, CLUB_ID);

    ReflectionTestUtils.setField(member,"id", MemberFixture.ID);

    application = new Application(applicationCreateRequest,club);

    applicant = new Applicant(applicantCreateRequest,application);

    ReflectionTestUtils.setField(club,"id", CLUB_ID);
    ReflectionTestUtils.setField(another,"id", CLUB_ID + 1L);
    ReflectionTestUtils.setField(application,"id", APPLICATION_ID);
    ReflectionTestUtils.setField(applicant,"id", APPLICANT_ID);

    evaluation = new Evaluation(evaluationCreateRequest,member,applicant,CLUB_ID);

    ReflectionTestUtils.setField(evaluation,"id", EVALUATION_ID);
    ReflectionTestUtils.setField(applicant,"application", application);
    ReflectionTestUtils.setField(evaluation,"applicant", applicant);
    ReflectionTestUtils.setField(evaluation,"clubId", CLUB_ID);


  }

  @DisplayName("?????? ?????? ??????")
  @Test
  public void SuccessCreateEvaluation(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));
    given(evaluationRepository.existsByApplicantIdAndMemberIdAndIsDeletedIsFalse(anyLong(),anyLong())).willReturn(false);
    given(evaluationRepository.save(any())).willReturn(evaluation);
    given(applicantRepositoryCustom.getRateByApplicantId(anyLong())).willReturn(APPLICANT_RATE);
    //?????? ?????? ??????
    ReflectionTestUtils.setField(member,"clubId",CLUB_ID);
    //when
    Long savedId = evaluationService.create(evaluationCreateRequest,MemberFixture.ID, APPLICANT_ID);

    //then
    assertThatCode(() -> evaluationService.create(evaluationCreateRequest,MemberFixture.ID, APPLICANT_ID)).doesNotThrowAnyException();

    assertThat(savedId).isEqualTo(EVALUATION_ID);
  }
  @DisplayName("?????? ?????? ?????? - ?????? ?????? ??????")
  @Test
  public void FailCreateEvaluation_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest,MemberFixture.ID, APPLICANT_ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("?????? ?????? ?????? - ????????? ?????? ??????")
  @Test
  public void FailCreateEvaluation_ApplicantNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest,MemberFixture.ID, APPLICANT_ID))
        .isInstanceOf(ApplicantNotFoundException.class);
  }
  @DisplayName("?????? ?????? ?????? - ?????? ????????? ??? ????????? ?????????")
  @Test
  public void FailCreateEvaluation_AlreadyExists(){
    //given, when
    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));
    given(evaluationRepository.existsByApplicantIdAndMemberIdAndIsDeletedIsFalse(anyLong(),anyLong())).willReturn(true);

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest,MemberFixture.ID, APPLICANT_ID))
        .isInstanceOf(AlreadyExistsException.class);
  }


  @DisplayName("?????? ?????? ?????? - ?????? ??????(????????? ???????????? ???????????? ????????? ??? ?????? ???????????? ?????? ??????)")
  @Test
  public void FailCreateEvaluation_UnAuthorized(){
    //given, when
    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //?????? ?????? ?????? ??????
    ReflectionTestUtils.setField(member,"clubId",another.getId());

    //then
    assertThatThrownBy(() -> evaluationService.create(evaluationCreateRequest,MemberFixture.ID, APPLICANT_ID))
        .isInstanceOf(NoPermissionCreateException.class);
  }

  @DisplayName("?????? ?????? ??????")
  @Test
  public void SuccessUpdateEvaluation(){
    //given

    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));
    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(evaluationRepository.save(any())).willReturn(evaluation);
    given(applicantRepositoryCustom.getRateByApplicantId(anyLong())).willReturn(APPLICANT_RATE);

    //when
    Long savedId = evaluationService.update(evaluationUpdateRequest,EVALUATION_ID,MemberFixture.ID);

    //then
    assertThatCode(() -> evaluationService.update(evaluationUpdateRequest,EVALUATION_ID,MemberFixture.ID)).doesNotThrowAnyException();

    assertThat(savedId).isEqualTo(EVALUATION_ID);
  }
  @DisplayName("?????? ?????? ?????? - ?????? ?????? ??????")
  @Test
  public void FailUpdateEvaluation_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));

    //then
    assertThatThrownBy(() -> evaluationService.update(evaluationUpdateRequest,EVALUATION_ID,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("?????? ?????? ?????? - ?????? ??????(????????? ???????????? ???????????? ????????? ??? ?????? ???????????? ?????? ??????)")
  @Test
  public void FailUpdateEvaluation_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));
    //?????? ?????? ?????? ??????
    ReflectionTestUtils.setField(evaluation,"creatorId", member.getId()+1L);
    //then
    assertThatThrownBy(() -> evaluationService.update(evaluationUpdateRequest,EVALUATION_ID,MemberFixture.ID))
        .isInstanceOf(NoPermissionUpdateException.class);
  }
  @DisplayName("?????? ?????? ??????")
  @Test
  public void SuccessDeleteEvaluation(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));
    given(evaluationRepository.save(any())).willReturn(evaluation);
    given(applicantRepositoryCustom.getRateByApplicantId(anyLong())).willReturn(APPLICANT_RATE);

    //when
    evaluationService.delete(EVALUATION_ID,MemberFixture.ID);

    //then
    assertThatCode(() -> evaluationService.delete(EVALUATION_ID,MemberFixture.ID)).doesNotThrowAnyException();

  }
  @DisplayName("?????? ?????? ?????? - ?????? ?????? ??????")
  @Test
  public void FailDeleteEvaluation_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));

    //then
    assertThatThrownBy(() -> evaluationService.delete(EVALUATION_ID,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("?????? ?????? ?????? - ?????? ??????(????????? ???????????? ???????????? ????????? ??? ?????? ???????????? ?????? ??????)")
  @Test
  public void FailDeleteEvaluation_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(evaluationRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(evaluation));

    //?????? ?????? ?????? ??????
    ReflectionTestUtils.setField(evaluation,"creatorId", member.getId()+1L);
    //then
    assertThatThrownBy(() -> evaluationService.delete(EVALUATION_ID,MemberFixture.ID))
        .isInstanceOf(NoPermissionDeleteException.class);
  }
  @DisplayName("???????????? ?????? ?????? ?????? ?????? ??????")
  @Test
  public void SuccessFindEvaluations_ApplicationID(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));
    given(evaluationRepository.findAllByApplicantIdAndIsDeletedIsFalse(anyLong())).willReturn(Arrays.asList(evaluation));

    //?????? ?????? ??????
    ReflectionTestUtils.setField(member,"clubId",CLUB_ID);    List<EvaluationResponse> expected = Arrays.asList(new EvaluationResponse(evaluation,member, APPLICATION_ID, APPLICANT_ID));
    //when
    List<EvaluationResponse> actual = evaluationService.findAllByApplication(APPLICANT_ID,MemberFixture.ID);

    //then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    assertThatCode(() -> evaluationService.findAllByApplication(APPLICANT_ID,MemberFixture.ID)).doesNotThrowAnyException();
  }
  @DisplayName("???????????? ?????? ?????? ?????? ?????? ?????? - ?????? ?????? ??????")
  @Test
  public void FailFindEvaluations_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //then
    assertThatThrownBy(() -> evaluationService.findAllByApplication(APPLICANT_ID,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("???????????? ?????? ?????? ?????? ?????? ?????? - ?????? ??????(????????? ???????????? ???????????? ????????? ??? ?????? ???????????? ?????? ??????)")
  @Test
  public void FailFindEvaluations_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //?????? ?????? ?????? ??????
    ReflectionTestUtils.setField(member,"clubId",another.getId());
    //then
    assertThatThrownBy(() -> evaluationService.findAllByApplication(APPLICANT_ID,MemberFixture.ID))
        .isInstanceOf(NoPermissionReadException.class);
  }

}
