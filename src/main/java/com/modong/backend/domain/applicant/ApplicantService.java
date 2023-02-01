package com.modong.backend.domain.applicant;

import static com.modong.backend.Enum.CustomCode.ERROR_REQ_PARAM_ID;

import com.modong.backend.domain.applicant.Dto.ApplicantDetailResponse;
import com.modong.backend.domain.applicant.Dto.ApplicantRequest;
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

  private final ApplicantRepositoryCustomImpl applicantRepositoryCustomImpl;
  private final ApplicationService applicationService;
  private final EssentialAnswerService essentialAnswerService;
  private final QuestionAnswerService questionAnswerService;
  public List<ApplicantSimpleResponse> findAllByApplicationId(Long applicationId) {
    List<ApplicantSimpleResponse> applicants = applicantRepository.findAllByApplicationId(applicationId).stream().map(
        ApplicantSimpleResponse::new).collect(
        Collectors.toList());
    return applicants;
  }


  // 지원자 상세조회
  public ApplicantDetailResponse findById(Long applicantId) {
    ApplicantDetailResponse applicant = new ApplicantDetailResponse(applicantRepository.findById(applicantId)
        .orElseThrow(()-> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString())));

    return applicant;
  }
  @Transactional // 지원자 상태 변경
  public Long changeApplicantStatus(Long applicantId, ChangeApplicantStatusRequest applicantStatus){

    Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));

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
  public Long createApplicant(ApplicantRequest applicantRequest) {


    Application application = applicationService.findSimpleById(applicantRequest.getApplicationId());

    Applicant applicant = new Applicant(applicantRequest, application);

    applicantRepository.save(applicant);

    //필수 질문 저장
    for(EssentialAnswerRequest essentialAnswerRequest : applicantRequest.getEssentialAnswers()){
      essentialAnswerService.create(essentialAnswerRequest,applicant);
    }

    //폼 질문 저장
    for(QuestionAnswerRequest questionAnswerRequest : applicantRequest.getQuestionAnswers()){
      questionAnswerService.create(questionAnswerRequest,applicant);
    }

    return applicant.getId();

  }

  public PageApplicantsResponse findAllByApplicationByIdAndStatus(Long applicationId, SearchApplicantRequest request,
      Pageable pageable) {
    Page<Applicant> result = applicantRepositoryCustomImpl.searchByApplicationIdAndStatus(applicationId,request,pageable);
    return new PageApplicantsResponse(result);

  }
}
