package com.modong.backend.unit.domain.club;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.ClubCheckRequest;
import com.modong.backend.domain.club.ClubService;
import com.modong.backend.domain.club.Dto.ClubCreateResponse;
import com.modong.backend.domain.club.Dto.ClubRequest;
import com.modong.backend.domain.club.Dto.ClubResponse;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import com.modong.backend.unit.base.ServiceTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

public class ClubServiceTest extends ServiceTest {

  @Autowired
  private ClubService clubService;

  private final String clubCode = "F1KAO132K";


  @BeforeEach
  public void init() {
  }

  @DisplayName("ClubCode 유효성 체크 - 동아리코드로 등록된 동아리가 없으면 ClubNotFoundException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfClubNotExist(){
    //given
    ClubCheckRequest clubCheckRequest = ClubCheckRequest.builder().ClubCode(clubCode).build();
    //when
    given(clubRepository.existsByClubCode(clubCode))
        .willReturn(true);
    //then
    assertThatThrownBy(() -> clubService.checkClubCode(clubCheckRequest))
        .isInstanceOf(ClubNotFoundException.class);
  }

  @DisplayName("ClubCode 유효성 체크 - 동아리코드로 등록된 동아리가 있으면 예외가 발생하지 않아야 한다.")
  @Test
  public void passIfClubExist(){
    //given,when
    ClubCheckRequest clubCheckRequest = ClubCheckRequest.builder().ClubCode(clubCode).build();

    given(clubRepository.existsByClubCode(clubCode))
        .willReturn(false);
    //then
    assertThatCode(() -> clubService.checkClubCode(clubCheckRequest))
        .doesNotThrowAnyException();
  }

  // 출처 - https://galid1.tistory.com/772 테스트는 독립적이여야 한다.
  @DisplayName("동아리 생성 - 동아리가 정상적으로 저장돼야한다.")
  @Test
  public void passSaveClub(){
    //given
    ClubRequest clubRequest = ClubRequest.builder().name("모동")
        .profileImgUrl("https://avatars.githubusercontent.com/u/38587274?v=4").build();
    Club club = new Club(clubRequest);

    Long fakeClubId = 1L;
    ReflectionTestUtils.setField(club,"id",fakeClubId);

    // mocking
    given(clubRepository.save(any()))
        .willReturn(club);

    //when
    ClubCreateResponse savedClub = clubService.save(clubRequest);


    //then
    assertThatCode(() -> clubService.save(clubRequest))
        .doesNotThrowAnyException();

    assertThat(savedClub).isNotNull();
  }
  @DisplayName("동아리 조회 - Id를 가진 동아리가 존재하면 동아리를 조회한다.")
  @Test
  public void findClubIfClubIdExist(){
    //given

    ClubRequest clubRequest = ClubRequest.builder().name("모동")
        .profileImgUrl("https://avatars.githubusercontent.com/u/38587274?v=4").build();
    Club club = new Club(clubRequest);

    Long findId = 1L;
    ReflectionTestUtils.setField(club,"id",findId);

    // mocking
    given(clubRepository.findById(any()))
        .willReturn(Optional.of(club));

    //when
    ClubResponse clubResponse = clubService.findById(findId);


    //then
    assertThat(clubResponse).usingRecursiveComparison()
        .isEqualTo(new ClubResponse(club));
  }
  @DisplayName("동아리 조회 - Id를 가진 동아리가 존재하지 않으면 ClubNotFoundException 가 발생한다..")
  @Test
  public void ThrowExceptionIfClubIdNotExist(){
    //given, when

    Long findId = 1L;

    given(clubRepository.findById(any()))
        .willReturn(Optional.empty());


    //then
    assertThatThrownBy(() -> clubService.findById(findId))
        .isInstanceOf(ClubNotFoundException.class);
  }
}
