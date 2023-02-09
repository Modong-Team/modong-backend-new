package com.modong.backend.unit.domain.member;

import static com.modong.backend.Fixtures.ClubFixture.CLUB_CODE;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_NAME;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_PROFILE_IMG_URL;
import static com.modong.backend.Fixtures.MemberFixture.EMAIL;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.NAME;
import static com.modong.backend.Fixtures.MemberFixture.PASSWORD;
import static com.modong.backend.Fixtures.MemberFixture.PHONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.modong.backend.auth.member.Dto.MemberRegisterRequest;
import com.modong.backend.auth.member.Dto.MemberCheckRequest;
import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.member.MemberService;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import com.modong.backend.unit.base.ServiceTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;


// 각 멤버 변수의 유효값 검사는 우리 Controller 에서 처리하는 책임을 가지게 하는 걸로 합니다.
public class MemberServiceTest extends ServiceTest {

  @Autowired
  private MemberService memberService;

  private MemberRegisterRequest memberRegisterRequest;

  private ClubCreateRequest clubCreateRequest;

  @BeforeEach
  public void init(){

    memberRegisterRequest = MemberRegisterRequest.builder()
        .memberId(MEMBER_ID).email(EMAIL).password(PASSWORD)
        .phone(PHONE).name(NAME).clubCode(CLUB_CODE).build();


    clubCreateRequest = ClubCreateRequest.builder().name(CLUB_NAME)
        .profileImgUrl(CLUB_PROFILE_IMG_URL).build();

  }

  @DisplayName("중복 ID 체크 - MemberId가 중복이면 checkMemberId 함수가 true 를 반환해야 한다.")
  @Test
  public void returnTrueMemberIdIsDuplicated(){
    //given
    boolean expected = true;

    MemberCheckRequest memberCheckRequest = MemberCheckRequest.builder().memberId(MEMBER_ID).build();

    given(memberRepository.existsByMemberId(MEMBER_ID))
        .willReturn(expected);
    //when
    boolean result = memberService.checkMemberId(memberCheckRequest);
    //then
    assertThat(expected).isEqualTo(result);
  }

  @DisplayName("중복 ID 체크  - MemberId가 중복되지 않으면 checkMemberId 함수가 false 를 반환해야 한다.")
  @Test
  public void returnFalseMemberIdIsNotDuplicated(){
    //given
    boolean expected = false;

    MemberCheckRequest memberCheckRequest = MemberCheckRequest.builder().memberId(MEMBER_ID).build();
    given(memberRepository.existsByMemberId(MEMBER_ID))
        .willReturn(expected);
    //when
    boolean result = memberService.checkMemberId(memberCheckRequest);
    //then
    assertThat(expected).isEqualTo(result);
  }
  @DisplayName("회원가입 - 동아리코드가 유효하면 정상적으로 가입이 완료돼야 한다. ")
  @Test
  public void returnIdIfRegisterRequestIsValid(){
    //given

    Club club = new Club(clubCreateRequest);

    Member member = new Member(memberRegisterRequest);
    member.setEncodedPassword(passwordEncoder.encode(memberRegisterRequest.getPassword()));

    Long fakeMemberId = 1L;
    ReflectionTestUtils.setField(member,"id",fakeMemberId);

    //동아리 코드 유효
    given(clubRepository.findByClubCode(memberRegisterRequest.getClubCode()))
        .willReturn(Optional.of(club));
    //중복 이메일
    given(memberRepository.existsByEmail(memberRegisterRequest.getEmail()))
        .willReturn(false);
    //중복 휴대폰
    given(memberRepository.existsByPhone(memberRegisterRequest.getPhone()))
        .willReturn(false);

    //회원 저장시 반환값
    given(memberRepository.save(any()))
        .willReturn(member);


    //when
    Long savedId = memberService.register(memberRegisterRequest);

    //then
    assertThatCode(() -> memberService.register(memberRegisterRequest))
        .doesNotThrowAnyException();

    assertThat(savedId).isNotNull();
  }

  @DisplayName("회원가입 - 동아리 코드가 유효하지 않으면 ClubNotFoundException 이 발생해야 한다. ")
  @Test
  public void throwErrorIFClubCodeIsNotValid(){
    //given,when

    //동아리 코드 유효 X
    given(clubRepository.findByClubCode(memberRegisterRequest.getClubCode()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> memberService.register(memberRegisterRequest))
        .isInstanceOf(ClubNotFoundException.class);

  }

  @DisplayName("회원 조회 - ClubId를 가진 동아리가 존재하지 않으면 ClubNotFoundException 가 발생한다..")
  @Test
  public void throwExceptionIfClubIdNotExist(){
    //given, when

    Long findId = 1L;

    given(clubRepository.findById(any()))
        .willReturn(Optional.empty());


    //then
    assertThatThrownBy(() -> memberService.findById(findId))
        .isInstanceOf(MemberNotFoundException.class);
  }

}
