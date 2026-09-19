package com.modong.backend.unit.domain.memo;

import static com.modong.backend.Fixtures.ApplicantFixture.APPLICANT_ID;
import static com.modong.backend.Fixtures.ApplicationFixture.APPLICATION_ID;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_ID;
import static com.modong.backend.Fixtures.MemoFixture.MEMO_ID;
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
import com.modong.backend.domain.memo.Memo;
import com.modong.backend.domain.memo.MemoService;
import com.modong.backend.domain.memo.dto.MemoResponse;
import com.modong.backend.global.exception.applicant.ApplicantNotFoundException;
import com.modong.backend.global.exception.auth.NoPermissionCreateException;
import com.modong.backend.global.exception.auth.NoPermissionDeleteException;
import com.modong.backend.global.exception.auth.NoPermissionReadException;
import com.modong.backend.global.exception.auth.NoPermissionUpdateException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import com.modong.backend.unit.base.ServiceTest;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;

public class MemoServiceTest extends ServiceTest {
  @Autowired
  private MemoService memoService;

  private Memo memo;
  private Club club,another;
  private Application application;
  private Applicant applicant;
  private Member member;

  @BeforeEach
  public void init(){

    club = new Club(clubCreateRequest);
    another = new Club(clubCreateRequest);

    member = new Member(memberRegisterRequest, CLUB_ID);

    application = new Application(applicationCreateRequest,club);

    applicant = new Applicant(applicantCreateRequest,application);


    ReflectionTestUtils.setField(club,"id", CLUB_ID);
    ReflectionTestUtils.setField(another,"id", CLUB_ID + 1L);
    ReflectionTestUtils.setField(member,"id", MemberFixture.ID);
    ReflectionTestUtils.setField(application,"id", APPLICATION_ID);
    ReflectionTestUtils.setField(applicant,"id", APPLICANT_ID);

    memo = new Memo(memoCreateRequest,member,applicant);

    ReflectionTestUtils.setField(memo,"id", MEMO_ID);
    ReflectionTestUtils.setField(applicant,"application", application);

  }

  @DisplayName("메모 생성 성공")
  @Test
  public void SuccessCreateMemo(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));
    given(memoRepository.save(any())).willReturn(memo);
    //생성 권한 주기
    ReflectionTestUtils.setField(member,"clubId",CLUB_ID);


    //when
    Long savedId = memoService.create(memoCreateRequest,MemberFixture.ID,APPLICANT_ID);

    //then
    assertThatCode(() -> memoService.create(memoCreateRequest,MemberFixture.ID,APPLICANT_ID)).doesNotThrowAnyException();

    assertThat(savedId).isEqualTo(MEMO_ID);
  }
  @DisplayName("메모 생성 실패 - 회원 조회 실패")
  @Test
  public void FailCreateMemo_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //then
    assertThatThrownBy(() -> memoService.create(memoCreateRequest,MemberFixture.ID,APPLICANT_ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("메모 생성 실패 - 지원자 조회 실패")
  @Test
  public void FailCreateMemo_ApplicantNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> memoService.create(memoCreateRequest,MemberFixture.ID,APPLICANT_ID))
        .isInstanceOf(ApplicantNotFoundException.class);
  }
  @DisplayName("메모 생성 실패 - 권한 없음(회원의 동아리와 지원서를 수정할 수 있는 동아리가 다를 경우)")
  @Test
  public void FailCreateMemo_UnAuthorized(){
    //given, when
    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //생성 권한 없는 경우
    ReflectionTestUtils.setField(member,"clubId",another.getId());

    //then
    assertThatThrownBy(() -> memoService.create(memoCreateRequest,MemberFixture.ID,APPLICANT_ID))
        .isInstanceOf(NoPermissionCreateException.class);
  }

  @DisplayName("메모 수정 성공")
  @Test
  public void SuccessUpdateMemo(){
    //given

    given(memoRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(memo));
    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(memoRepository.save(any())).willReturn(memo);

    //when
    Long savedId = memoService.update(memoUpdateRequest,MEMO_ID,MemberFixture.ID);

    //then
    assertThatCode(() -> memoService.update(memoUpdateRequest,MEMO_ID,MemberFixture.ID)).doesNotThrowAnyException();

    assertThat(savedId).isEqualTo(MEMO_ID);
  }
  @DisplayName("메모 수정 실패 - 회원 조회 실패")
  @Test
  public void FailUpdateMemo_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(memoRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(memo));

    //then
    assertThatThrownBy(() -> memoService.update(memoUpdateRequest,MEMO_ID,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("메모 수정 실패 - 권한 없음(회원의 동아리와 지원서를 수정할 수 있는 동아리가 다를 경우)")
  @Test
  public void FailUpdateMemo_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(memoRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(memo));
    //수정 권한 없게 설정
    ReflectionTestUtils.setField(memo,"creatorId", member.getId()+1L);
    //then
    assertThatThrownBy(() -> memoService.update(memoUpdateRequest,MEMO_ID,MemberFixture.ID))
        .isInstanceOf(NoPermissionUpdateException.class);
  }
  @DisplayName("메모 삭제 성공")
  @Test
  public void SuccessDeleteMemo(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(memoRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(memo));
    given(memoRepository.save(any())).willReturn(memo);

    //when
    memoService.delete(MEMO_ID,MemberFixture.ID);

    //then
    assertThatCode(() -> memoService.delete(MEMO_ID,MemberFixture.ID)).doesNotThrowAnyException();

  }
  @DisplayName("메모 삭제 실패 - 회원 조회 실패")
  @Test
  public void FailDeleteMemo_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(memoRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(memo));

    //then
    assertThatThrownBy(() -> memoService.delete(MEMO_ID,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("메모 삭제 실패 - 권한 없음(회원의 동아리와 지원서를 수정할 수 있는 동아리가 다를 경우)")
  @Test
  public void FailDeleteMemo_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(memoRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(memo));

    //삭제 권한 없게 설정
    ReflectionTestUtils.setField(memo,"creatorId", member.getId()+1L);
    //then
    assertThatThrownBy(() -> memoService.delete(MEMO_ID,MemberFixture.ID))
        .isInstanceOf(NoPermissionDeleteException.class);
  }
  @DisplayName("지원자에 대한 모든 메모 조회 성공")
  @Test
  public void SuccessFindMemos_ApplicationID(){
    //given

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));
    given(memoRepository.findAllByApplicantIdAndIsDeletedIsFalse(anyLong())).willReturn(Arrays.asList(memo));

    //조회 권한 주기
    ReflectionTestUtils.setField(member,"clubId",CLUB_ID);

    List<MemoResponse> expected = Arrays.asList(new MemoResponse(memo,member, APPLICATION_ID, APPLICANT_ID));
    //when
    List<MemoResponse> actual = memoService.findAllByApplication(APPLICANT_ID,MemberFixture.ID);

    //then
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    assertThatCode(() -> memoService.findAllByApplication(APPLICANT_ID,MemberFixture.ID)).doesNotThrowAnyException();
  }
  @DisplayName("지원자에 대한 모든 메모 조회 실패 - 회원 조회 실패")
  @Test
  public void FailFindMemos_MemberNotFound(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.empty());
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //then
    assertThatThrownBy(() -> memoService.findAllByApplication(APPLICANT_ID,MemberFixture.ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("지원자에 대한 모든 메모 조회 실패 - 권한 없음(회원의 동아리와 지원서를 수정할 수 있는 동아리가 다를 경우)")
  @Test
  public void FailFindMemos_UnAuthorized(){
    //given, when

    given(memberRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(member));
    given(applicantRepository.findByIdAndIsDeletedIsFalse(anyLong())).willReturn(Optional.of(applicant));

    //조회 권한 없게 설정
    ReflectionTestUtils.setField(member,"clubId",another.getId());
    //then
    assertThatThrownBy(() -> memoService.findAllByApplication(APPLICANT_ID,MemberFixture.ID))
        .isInstanceOf(NoPermissionReadException.class);
  }
}
