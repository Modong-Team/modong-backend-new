package com.modong.backend.domain.application;

import static com.modong.backend.Enum.CustomCode.ERROR_REQ_PARAM_ID;

import com.modong.backend.domain.application.Dto.ApplicationRequest;
import com.modong.backend.domain.application.Dto.ApplicationDetailResponse;
import com.modong.backend.domain.application.Dto.ApplicationSimpleResponse;
import com.modong.backend.domain.applicationEssential.ApplicationEssential;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.ClubRepository;
import com.modong.backend.domain.club.ClubService;
import com.modong.backend.domain.essentialQuestion.Dto.EssentialQuestionResponse;
import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
import com.modong.backend.domain.essentialQuestion.EssentialQuestionService;
import com.modong.backend.domain.form.Form;
import com.modong.backend.domain.form.dto.FormResponse;
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

    Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));

    return getDetailResponse(application);
  }

  public Application findSimpleById(Long applicationId){
    Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));
    return application;
  }

  public List<ApplicationSimpleResponse> findAllByClubId(Long clubId) {
    List<ApplicationSimpleResponse> applications = applicationRepository.findAllByClubId(clubId).stream().map(ApplicationSimpleResponse::new).collect(
        Collectors.toList());
    return applications;
  }

  @Transactional
  public Long createApplication(ApplicationRequest applicationRequest) {

    Club club = clubRepository.findById(applicationRequest.getClubId())
        .orElseThrow(() -> new ClubNotFoundException(applicationRequest.getClubId()));

    Application application = new Application(applicationRequest,club);

    //지원서 링크 중복 검사
    if(applicationRepository.findByUrlId(applicationRequest.getUrlId()).isPresent()){
      throw new IllegalArgumentException("이미 존재하는 링크 ID 입니다.");
    }

    //필수 질문 id로 저장
    for(Long id : applicationRequest.getEssentialQuestionIds()){
      EssentialQuestion essentialQuestion = essentialQuestionService.findById(id);
      ApplicationEssential applicationEssential = new ApplicationEssential(application,essentialQuestion);
      application.addEssential(applicationEssential);
    }

    applicationRepository.save(application);

    //필수 질문 저장
//    for(EssentialQuestionRequest essentialQuestionRequest : applicationRequest.getEssentialQuestions()){
//      essentialQuestionService.create(essentialQuestionRequest,application);
//    }

//    //페이지 저장
//    for(FormRequest formRequest : applicationRequest.getForms()){
//      formService.create(formRequest,application);
//    }

    return application.getId();
  }



  @Transactional
  //필수 질문 id로 업데이트
  public Long updateEssentialQuestion(Long applicationId, ApplicationRequest applicationRequest) {

    Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));

    application.update(applicationRequest);
    //기존 필수 질문들 삭제
    application.getEssentials().removeAll(application.getEssentials());

    for(Long id : applicationRequest.getEssentialQuestionIds()){
      EssentialQuestion essentialQuestion = essentialQuestionService.findById(id);
      ApplicationEssential applicationEssential = new ApplicationEssential(application,essentialQuestion);
      application.addEssential(applicationEssential);
    }
    applicationRepository.save(application);


    return application.getId();
  }



  @Transactional
  public void deleteApplication(Long applicationId) {
    Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));
    applicationRepository.delete(application);
  }

  public ApplicationDetailResponse findDetailByUrlId(String urlId) {
    Application application = applicationRepository.findByUrlId(urlId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));

    return getDetailResponse(application);

  }

  private ApplicationDetailResponse getDetailResponse(Application application) {
    ApplicationDetailResponse response = new ApplicationDetailResponse(application);

    for(ApplicationEssential applicationEssential : application.getEssentials()){
      response.addEssentialQuestion(new EssentialQuestionResponse(applicationEssential.getEssentialQuestion()));
      System.out.println(applicationEssential.getEssentialQuestion().getContent());
    }

    for(Form form : application.getForms()){
      response.addForm(new FormResponse(form));
    }

    return response;
  }
}
