package com.modong.backend.Applicant;

import com.modong.backend.Applicant.Dto.ApplicantDetailResponse;
import com.modong.backend.Applicant.Dto.ApplicantRequest;
import com.modong.backend.Applicant.Dto.ApplicantSimpleResponse;
import com.modong.backend.Applicant.Dto.ChangeApplicantStatusRequest;
import com.modong.backend.Base.Dto.SavedId;
import com.modong.backend.Enum.MessageCode;
import com.modong.backend.Base.Dto.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Api(tags =  "지원자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApplicantController {
  private final ApplicantService applicantService;

  @GetMapping("/applicants/{application_id}") // 메인 화면에서 사용할 작성한 지원서 마다 어떤 지원자가 지원했는지 조회할때 사용
  @Operation(summary = "지원자들 간편 조회", description = "지원서의 ID를 통해 모든 지원자들을 간편 조회 한다.")
  public ResponseEntity getApplicantsByApplicationId(@Validated @PathVariable(name="application_id") Long applicationId){
    List<ApplicantSimpleResponse> applicants = applicantService.findAllByApplicationId(applicationId);
    return ResponseEntity.ok(new BaseResponse(applicants, HttpStatus.OK.value(), MessageCode.SUCCESS_GET_LIST));
  }

  @GetMapping("/applicant/{applicant_id}") // 각 지원자를 ID로 조회해 질문에 어떤 답을 했는지 알고 싶을때 사용하는 API
  @Operation(summary = "지원자 답변 조회", description = "지원자를 ID로 조회해 질문에 어떤 답을 했는지 조회 한다.")
  public ResponseEntity getApplicantById(@Validated @PathVariable(name="applicant_id") Long applicantId){
    ApplicantDetailResponse applicant = applicantService.findById(applicantId);
    return ResponseEntity.ok(new BaseResponse(applicant, HttpStatus.OK.value(),MessageCode.SUCCESS_GET));
  }

  @PatchMapping("/applicant/{applicant_id}")// 지원자의상태를 변경할때 사용하는 API
  @Operation(summary = "지원자 상태 변경", description = "지원자를 상태를 변경한다. FAIL(1),ACCEPT(2),APPLICATION(3),INTERVIEW(4),SUCCESS(5)")
  public ResponseEntity changeApplicantStatus(@Validated @PathVariable(name="applicant_id") Long applicantId, @RequestBody ChangeApplicantStatusRequest applicantStatus){
    SavedId savedId = new SavedId(applicantService.changeApplicantStatus(applicantId,applicantStatus));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.OK.value(), MessageCode.SUCCESS_UPDATE));
  }

  @PostMapping("/applicant")//지원자 생성과 동시에 답변들 저장하는 API
  @Operation(summary = "지원자 생성", description = "지원자를 생성하고 답변을 저장한다.")
  public ResponseEntity createApplicantAndSaveQuestions(@Validated @RequestBody ApplicantRequest applicantRequest){
    SavedId savedId = new SavedId(applicantService.createApplicant(applicantRequest));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.CREATED.value(), MessageCode.SUCCESS_CREATE));
  }
}
