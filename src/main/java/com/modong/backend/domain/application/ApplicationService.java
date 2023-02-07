package com.modong.backend.domain.application;

import com.modong.backend.Enum.StatusCode;
import com.modong.backend.domain.application.Dto.ApplicationCreateRequest;
import com.modong.backend.domain.application.Dto.ApplicationDetailResponse;
import com.modong.backend.domain.application.Dto.ApplicationSimpleResponse;
import com.modong.backend.domain.application.Dto.ApplicationUpdateRequest;
import com.modong.backend.domain.applicationEssential.ApplicationEssential;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.ClubRepository;
import com.modong.backend.domain.essentialQuestion.Dto.EssentialQuestionResponse;
import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
import com.modong.backend.domain.essentialQuestion.EssentialQuestionService;
import com.modong.backend.domain.form.Form;
import com.modong.backend.domain.form.dto.FormResponse;
import com.modong.backend.global.exception.application.ApplicationNotFoundException;
import com.modong.backend.global.exception.application.UrlIdDuplicateException;
import com.modong.backend.global.exception.StatusBadRequestException;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

  private final ApplicationRepository applicationRepository;
  private final ClubRepository clubRepository;
  private final EssentialQuestionService essentialQuestionService;

  public ApplicationDetailResponse findDetailById(Long applicationId) {

    Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));

    return getDetailResponse(application);
  }

  public Application findSimpleById(Long applicationId){
    Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));
    return application;
  }

  public List<ApplicationSimpleResponse> findAllByClubId(Long clubId) {
    List<ApplicationSimpleResponse> applications = applicationRepository.findAllByClubId(clubId).stream().map(ApplicationSimpleResponse::new).collect(
        Collectors.toList());
    return applications;
  }

  @Transactional
  public Long createApplication(ApplicationCreateRequest applicationCreateRequest) {

    Club club = clubRepository.findById(applicationCreateRequest.getClubId())
        .orElseThrow(() -> new ClubNotFoundException(applicationCreateRequest.getClubId()));

    Application application = new Application(applicationCreateRequest,club);

    //지원서 링크 중복 검사
    if(applicationRepository.existsByUrlId(applicationCreateRequest.getUrlId())){
      throw new UrlIdDuplicateException();
    }

    //필수 질문 id로 저장
    setEssentialQuestion(applicationCreateRequest.getEssentialQuestionIds(), application);

    Application saved = applicationRepository.save(application);

    return saved.getId();
  }

  private void setEssentialQuestion(List<Long> applicationCreateRequest, Application application) {
    for(Long id : applicationCreateRequest){
      EssentialQuestion essentialQuestion = essentialQuestionService.findById(id);
      ApplicationEssential applicationEssential = new ApplicationEssential(application,essentialQuestion);
      application.addEssential(applicationEssential);
    }
  }


  @Transactional
  //필수 질문 id로 업데이트
  public Long updateApplication(Long applicationId, ApplicationUpdateRequest applicationUpdateRequest) {

    Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));

    application.update(applicationUpdateRequest);
    //기존 필수 질문들 삭제
    application.getEssentials().removeAll(application.getEssentials());
    //필수 질문 설정
    setEssentialQuestion(applicationUpdateRequest.getEssentialQuestionIds(), application);

    Application saved = applicationRepository.save(application);


    return saved.getId();
  }



  @Transactional
  public void deleteApplication(Long applicationId) {
    Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));
    applicationRepository.delete(application);
  }

  public ApplicationDetailResponse findDetailByUrlId(String urlId) {
    Application application = applicationRepository.findByUrlId(urlId).orElseThrow(() -> new ApplicationNotFoundException(urlId));

    return getDetailResponse(application);

  }

  private ApplicationDetailResponse getDetailResponse(Application application) {
    ApplicationDetailResponse response = new ApplicationDetailResponse(application);

    for(ApplicationEssential applicationEssential : application.getEssentials()){
      response.addEssentialQuestion(new EssentialQuestionResponse(applicationEssential.getEssentialQuestion()));
    }

    for(Form form : application.getForms()){
      response.addForm(new FormResponse(form));
    }

    return response;
  }

  @Transactional
  public Long close(Long applicationId) {
    Application application = findSimpleById(applicationId);
    if(application.eqStatus(StatusCode.CLOSE)){
      throw new StatusBadRequestException();
    }
    application.close();
    Application saved = applicationRepository.save(application);
    return saved.getId();
  }

  @Transactional
  public Long open(Long applicationId) {
    Application application = findSimpleById(applicationId);
    if(application.eqStatus(StatusCode.OPEN)){
      throw new StatusBadRequestException();
    }
    application.open();
    Application saved = applicationRepository.save(application);
    return saved.getId();
  }
}
