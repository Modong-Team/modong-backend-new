package com.modong.backend.domain.applicant;

import com.modong.backend.domain.applicant.Dto.ApplicantDetailResponse;
import com.modong.backend.domain.applicant.Dto.ApplicantCreateRequest;
import com.modong.backend.domain.applicant.Dto.ApplicantSimpleResponse;
import com.modong.backend.domain.applicant.Dto.ChangeApplicantStatusRequest;
import com.modong.backend.domain.applicant.Dto.PageApplicantsResponse;
import com.modong.backend.domain.applicant.Dto.SearchApplicantRequest;
import com.modong.backend.domain.applicant.repository.ApplicantRepository;
import com.modong.backend.domain.applicant.repository.ApplicantRepositoryCustomImpl;
import com.modong.backend.domain.application.Application;
import com.modong.backend.domain.application.ApplicationService;
import com.modong.backend.Enum.ApplicantStatus;
import com.modong.backend.domain.essentialAnswer.Dto.EssentialAnswerRequest;
import com.modong.backend.domain.essentialAnswer.EssentialAnswerService;
import com.modong.backend.domain.questionAnswer.Dto.QuestionAnswerRequest;
import com.modong.backend.domain.questionAnswer.QuestionAnswerService;
import com.modong.backend.global.exception.IsClosed;
import com.modong.backend.global.exception.ResourceNotFoundException;
import com.modong.backend.global.exception.StatusBadRequestException;
import com.modong.backend.global.exception.applicant.ApplicantNotFoundException;
import java.util.stream.Collectors;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicantService {

  private final ApplicantRepository applicantRepository;
  private final ApplicationService applicationService;
  private final EssentialAnswerService essentialAnswerService;
  private final QuestionAnswerService questionAnswerService;

  private final ApplicantRepositoryCustomImpl applicantRepositoryCustom;

  public List<ApplicantSimpleResponse> findAllByApplicationId(Long applicationId) {
    List<ApplicantSimpleResponse> applicants = applicantRepository.findAllByApplicationId(applicationId).stream().map(
        ApplicantSimpleResponse::new).collect(
        Collectors.toList());
    return applicants;
  }


  // 지원자 상세조회
  public ApplicantDetailResponse findById(Long applicantId) {
    ApplicantDetailResponse applicant = new ApplicantDetailResponse(applicantRepository.findById(applicantId)
        .orElseThrow(()-> new ApplicantNotFoundException(applicantId)));

    return applicant;
  }
  @Transactional // 지원자 상태 변경
  public Long changeApplicantStatus(Long applicantId, ChangeApplicantStatusRequest applicantStatus){

    Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(() -> new ApplicantNotFoundException(applicantId));

    //Fail 이라면 현재 지원자가 어떤 상태인지는 상관하지 않고 변수 하나 추가해서 2가지 상태를 저장하도록 로직 변경
    if(applicantStatus.getApplicantStatusCode() == ApplicantStatus.FAIL.getCode()){
      applicant.fail();
    }
    else {
      applicant.changeStatus(ApplicantStatus.valueOf(applicantStatus.getApplicantStatusCode()));
    }
    applicantRepository.save(applicant);

    return applicant.getId();
  }
  @Transactional // 지원자 생성 및 질문에 대한 답변들 저장
  public Long createApplicant(ApplicantCreateRequest applicantCreateRequest) {


    Application application = applicationService.findSimpleById(applicantCreateRequest.getApplicationId());

    if(application.checkApplicationClosed()){
      throw new IsClosed();
    }

    Applicant applicant = new Applicant(applicantCreateRequest, application);

    applicantRepository.save(applicant);

    //필수 질문 저장
    for(EssentialAnswerRequest essentialAnswerRequest : applicantCreateRequest.getEssentialAnswers()){
      essentialAnswerService.create(essentialAnswerRequest,applicant);
    }

    //폼 질문 저장
    for(QuestionAnswerRequest questionAnswerRequest : applicantCreateRequest.getQuestionAnswers()){
      questionAnswerService.create(questionAnswerRequest,applicant);
    }

    return applicant.getId();

  }

  public PageApplicantsResponse filterByCondition(Long applicationId,
      SearchApplicantRequest searchApplicantRequest, Pageable pageable) {
    Page<Applicant> applicants = applicantRepositoryCustom.searchByApplicationIdAndStatus(applicationId,searchApplicantRequest,pageable);
//    if(applicants.isEmpty()){
//      throw new ResourceNotFoundException("조건에 맞는 지원자들 조회 결과 없음");
//    }
    return new PageApplicantsResponse(applicants);
  }

  @Transactional
  public Long cancelFailStatus(Long applicantId) {
    Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(() -> new ApplicantNotFoundException(applicantId));
    if(applicant.isFail() == false){//탈락한 상태가 아니라면 예외 처리
      throw new StatusBadRequestException();
    }
    else{
      applicant.cancelFail();
    }
    Applicant saved = applicantRepository.save(applicant);
    return saved.getId();
  }
}
