package com.modong.backend.Applicant;

import static com.modong.backend.Enum.MessageCode.ERROR_REQ_PARAM_ID;

import com.modong.backend.Applicant.Dto.ApplicantDetailResponse;
import com.modong.backend.Applicant.Dto.ApplicantRequest;
import com.modong.backend.Applicant.Dto.ApplicantSimpleResponse;
import com.modong.backend.Applicant.Dto.ChangeApplicantStatusRequest;
import com.modong.backend.Application.Application;
import com.modong.backend.Application.ApplicationService;
import com.modong.backend.Enum.ApplicantStatus;
import com.modong.backend.EssentialAnswer.Dto.EssentialAnswerRequest;
import com.modong.backend.EssentialAnswer.EssentialAnswerService;
import com.modong.backend.QuestionAnswer.Dto.QuestionAnswerRequest;
import com.modong.backend.QuestionAnswer.QuestionAnswerService;
import java.util.stream.Collectors;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    applicant.changeStatus(ApplicantStatus.valueOf(applicantStatus.getApplicantStatusCode()));

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
}
