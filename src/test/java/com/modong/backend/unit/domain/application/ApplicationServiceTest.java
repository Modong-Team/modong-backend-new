package com.modong.backend.unit.domain.application;

import static com.modong.backend.Fixtures.ApplicationFixture.APPLICATION_ID;
import static com.modong.backend.Fixtures.ApplicationFixture.URL_ID;
import static com.modong.backend.Fixtures.EssentialQuestionFixture.CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.modong.backend.domain.application.Application;
import com.modong.backend.domain.application.ApplicationService;
import com.modong.backend.domain.application.Dto.ApplicationDetailResponse;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
import com.modong.backend.global.exception.Application.ApplicationNotFoundException;
import com.modong.backend.global.exception.Application.UrlIdDuplicateException;
import com.modong.backend.global.exception.StatusBadRequestException;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import com.modong.backend.unit.base.ServiceTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

public class ApplicationServiceTest extends ServiceTest {

  @Autowired
  private ApplicationService applicationService;

  private Club club;
  private Application application, updatedApplication;

  private EssentialQuestion essentialQuestion;
  @BeforeEach
  public void init(){

    club = new Club(clubCreateRequest);

    application = new Application(applicationCreateRequest,club);

    updatedApplication = new Application(applicationCreateRequest,club);

    essentialQuestion = new EssentialQuestion(CONTENT,true);

    ReflectionTestUtils.setField(application,"id",APPLICATION_ID);
  }
  @DisplayName("지원서 생성 성공")
  @Test
  public void SuccessCreateApplication(){
    //given
    ReflectionTestUtils.setField(club,"id",APPLICATION_ID);

    given(clubRepository.findById(anyLong())).willReturn(Optional.of(club));
    given(essentialQuestionRepository.findById(anyLong())).willReturn(Optional.of(essentialQuestion));
    given(applicationRepository.save(any())).willReturn(application);

    //when
    Long savedId = applicationService.createApplication(applicationCreateRequest);

    //then
    assertThatCode(() -> applicationService.createApplication(applicationCreateRequest)).doesNotThrowAnyException();

    assertThat(savedId).isEqualTo(APPLICATION_ID);
  }
  //@DisplayName("지원서 생성 실패 - club 에 있는 회원이 아니라면 권한 관련 에러를 던진다.")
  @DisplayName("지원서 생성 실패 - club id를 가진 클럽이 없을 경우 ClubNotFoundException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfIdNotFound_Create(){
    //given, when

    given(clubRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> applicationService.createApplication(applicationCreateRequest))
        .isInstanceOf(ClubNotFoundException.class);
  }
  @DisplayName("지원서 생성 실패 - 지원서의 UrlId가 중복이라면 UrlIdDuplicateException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfUrlIdIsDuplicate(){
    //given, when

    given(clubRepository.findById(anyLong())).willReturn(Optional.of(club));

    given(applicationRepository.existsByUrlId(anyString()))
        .willReturn(true);
    //then
    assertThatThrownBy(() -> applicationService.createApplication(applicationCreateRequest))
        .isInstanceOf(UrlIdDuplicateException.class);
  }
  @DisplayName("지원서 상세 조회 성공 (Id)")
  @Test
  public void SuccessReadApplication_Id(){
    //given

    given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));

    //when
    ApplicationDetailResponse response = applicationService.findDetailById(APPLICATION_ID);

    //then
    assertThatCode(() -> applicationService.findDetailById(APPLICATION_ID)).doesNotThrowAnyException();

    assertThat(response).usingRecursiveComparison().isEqualTo(new ApplicationDetailResponse(application));
  }
  @DisplayName("지원서 상세 조회 성공 (UrlId)")
  @Test
  public void SuccessReadApplication_UrlID(){
    //given

    given(applicationRepository.findByUrlId(anyString())).willReturn(Optional.of(application));

    //when
    ApplicationDetailResponse response = applicationService.findDetailByUrlId(URL_ID);

    //then
    assertThatCode(() -> applicationService.findDetailByUrlId(URL_ID)).doesNotThrowAnyException();

    assertThat(response).usingRecursiveComparison().isEqualTo(new ApplicationDetailResponse(application));
  }
  @DisplayName("지원서 상세 조회 실패 (Id)- 요청한 id를 가진 지원서가 없을 경우 ApplicationNotFoundException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfIdNotFound_Read(){
    //given, when
    given(applicationRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> applicationService.findDetailById(APPLICATION_ID))
        .isInstanceOf(ApplicationNotFoundException.class);
  }
  @DisplayName("지원서 상세 조회 실패 (UrlId)- 요청한 urlId를 가진 지원서가 없을 경우 ApplicationNotFoundException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfUrlIdNotFound_Read(){
    //given, when
    given(applicationRepository.findByUrlId(anyString()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> applicationService.findDetailByUrlId(URL_ID))
        .isInstanceOf(ApplicationNotFoundException.class);
  }

  @DisplayName("지원서 업데이트 성공")
  @Test
  public void SuccessUpdateApplication(){
    //given
    ReflectionTestUtils.setField(updatedApplication,"id",APPLICATION_ID);

    given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));
    given(essentialQuestionRepository.findById(anyLong())).willReturn(Optional.of(essentialQuestion));
    given(applicationRepository.save(any())).willReturn(updatedApplication);
    //when

    Long savedId = applicationService.updateApplication(APPLICATION_ID,applicationUpdateRequest);

    //then
    assertThat(savedId).isEqualTo(APPLICATION_ID);

    assertThatCode(() -> applicationService.updateApplication(APPLICATION_ID,applicationUpdateRequest)).doesNotThrowAnyException();


  }
  //@DisplayName("지원서 업데이트 실패 - club에 있는 회원이 아니라면 권한 관련 에러를 던진다.")
  @DisplayName("지원서 업데이트 실패 - 요청한 id를 가진 지원서가 없을 경우 ApplicationNotFoundException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfIdNotFound_Update(){
    //given, when
    given(applicationRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> applicationService.updateApplication(APPLICATION_ID,applicationUpdateRequest))
        .isInstanceOf(ApplicationNotFoundException.class);
  }

  //@DisplayName("지원서 삭제 성공")
  @DisplayName("지원서 삭제 실패 - 요청한 id를 가진 지원서가 없을 경우 ApplicationNotFoundException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfIdNotFound_Delete(){
    //given, when
    given(applicationRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> applicationService.deleteApplication(APPLICATION_ID))
        .isInstanceOf(ApplicationNotFoundException.class);
  }

//  @DisplayName("지원서 마감 상태로 변경 성공")
//  @Test
//  public void SuccessApplicationClose(){
//    //given
//    ReflectionTestUtils.setField(updatedApplication,"id",APPLICATION_ID);
//    application.open();
//    updatedApplication.close();
//    given(applicationRepository.findById(anyLong()))
//        .willReturn(Optional.of(application));
//
//    given(applicationRepository.save(any()))
//        .willReturn(Optional.of(updatedApplication));
//
//    //when
//    Long savedId = applicationService.close(APPLICATION_ID);
//
//    //then
//
//    assertThat(savedId).isNotNull();
//
//    assertThatCode(() -> applicationService.close(APPLICATION_ID)).doesNotThrowAnyException();
//
//  }

  //@DisplayName("지원서 마감 상태로 변경 실패 - 유요한 요청이지만 여러번 요청이 올 경우")
  @DisplayName("지원서 마감 상태로 변경 실패 - 요청한 id를 가진 지원서가 없을 경우 ApplicationNotFoundException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfIdNotFound_Close(){
    //given, when
    application.open();
    given(applicationRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> applicationService.close(APPLICATION_ID))
        .isInstanceOf(ApplicationNotFoundException.class);
  }

  @DisplayName("지원서 마감 상태로 변경 실패 - 이미 마감한 상태일 경우 StatusBadRequestException 가 발생해야 한다.")
  @Test
  public void throwBadRequestException_Close(){
    //given, when
    application.close();
    given(applicationRepository.findById(anyLong()))
        .willReturn(Optional.of(application));

    //then
    assertThatThrownBy(() -> applicationService.close(APPLICATION_ID))
        .isInstanceOf(StatusBadRequestException.class);
  }

//  @DisplayName("지원서 모집 상태로 변경 성공")
//  @Test
//  public void SuccessApplicationOpen(){
//    //given
//    ReflectionTestUtils.setField(updatedApplication,"id",APPLICATION_ID);
//
//    application.close();
//    updatedApplication.open();
//
//    given(applicationRepository.findById(anyLong()))
//        .willReturn(Optional.of(application));
//
//    given(applicationRepository.save(any()))
//        .willReturn(Optional.of(updatedApplication));
//    //when
//    Long savedId = applicationService.open(APPLICATION_ID);
//
//    //then
//    assertThatCode(() -> applicationService.open(APPLICATION_ID)).doesNotThrowAnyException();
//    assertThat(savedId).isEqualTo(application.getId());
//  }

  @DisplayName("지원서 모집 상태로 변경 실패 - 요청한 id를 가진 지원서가 없을 경우 ApplicationNotFoundException 가 발생해야 한다.")
  @Test
  public void throwExceptionIfIdNotFound_Open(){
    //given, when
    given(applicationRepository.findById(anyLong()))
        .willReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> applicationService.open(APPLICATION_ID))
        .isInstanceOf(ApplicationNotFoundException.class);
  }

  @DisplayName("지원서 모집 상태로 변경 실패 - 이미 모집인 상태일 경우 StatusBadRequestException 가 발생해야 한다.")
  @Test
  public void throwBadRequestException_Open(){
    //given, when

    application.open();

    given(applicationRepository.findById(anyLong()))
        .willReturn(Optional.of(application));

    //then
    assertThatThrownBy(() -> applicationService.open(APPLICATION_ID))
        .isInstanceOf(StatusBadRequestException.class);
  }

}
