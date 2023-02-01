package com.modong.backend.domain.applicant;

import com.modong.backend.domain.applicant.Dto.ApplicantDetailResponse;
import com.modong.backend.domain.applicant.Dto.ApplicantRequest;
import com.modong.backend.domain.applicant.Dto.ApplicantSimpleResponse;
import com.modong.backend.domain.applicant.Dto.ChangeApplicantStatusRequest;
import com.modong.backend.base.Dto.SavedId;
import com.modong.backend.Enum.CustomCode;
import com.modong.backend.base.Dto.BaseResponse;
import com.modong.backend.domain.applicant.Dto.PageApplicantsResponse;
import com.modong.backend.domain.applicant.Dto.SearchApplicantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name =  "지원자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApplicantController {
  private final ApplicantService applicantService;

//  @GetMapping("/applicants/{application_id}") // 메인 화면에서 사용할 작성한 지원서 마다 어떤 지원자가 지원했는지 조회할때 사용
//  @Operation(summary = "지원자들 간편 조회", description = "지원서의 ID를 통해 모든 지원자들을 간편 조회 한다.", responses = {
//      @ApiResponse(responseCode = "200", description = "지원자들 간편 조회(리스트 반환)", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ApplicantSimpleResponse.class))))
//  })
//  public ResponseEntity getApplicantsByApplicationId(@Validated @PathVariable(name="application_id") Long applicationId){
//    List<ApplicantSimpleResponse> applicants = applicantService.findAllByApplicationId(applicationId);
//    return ResponseEntity.ok(new BaseResponse(applicants, HttpStatus.OK.value(), CustomCode.SUCCESS_GET_LIST));
//  }

  @GetMapping("/applicants/{application_id}") // 메인 화면에서 사용할 작성한 지원서 마다 어떤 지원자가 지원했는지 조회할때 사용
  @Operation(summary = "지원자들 간편 조회", description = "지원서의 ID와 지원자의 상태 코드를 통해 모든 지원자들을 간편 조회 한다.", responses = {
      @ApiResponse(responseCode = "200", description = "지원자들 간편 조회(리스트 반환)", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PageApplicantsResponse.class))))
  })
  public ResponseEntity getApplicants(@Validated @PathVariable(name="application_id") Long applicationId, @Validated SearchApplicantRequest searchApplicantRequest, @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
    PageApplicantsResponse applicants = applicantService.findAllByApplicationByIdAndStatus(applicationId, searchApplicantRequest, pageable);
    return ResponseEntity.ok(new BaseResponse(applicants, HttpStatus.OK.value(), CustomCode.SUCCESS_GET_LIST));
  }


  @GetMapping("/applicant/{applicant_id}") // 각 지원자를 ID로 조회해 질문에 어떤 답을 했는지 알고 싶을때 사용하는 API
  @Operation(summary = "지원자 답변 조회", description = "지원자를 ID로 조회해 질문에 어떤 답을 했는지 조회 한다.", responses = {
      @ApiResponse(responseCode = "200", description = "지원자 조회 성공", content = @Content(schema = @Schema(implementation = ApplicantDetailResponse.class)))
  })
  public ResponseEntity getApplicantById(@Validated @PathVariable(name="applicant_id") Long applicantId){
    ApplicantDetailResponse applicant = applicantService.findById(applicantId);
    return ResponseEntity.ok(new BaseResponse(applicant, HttpStatus.OK.value(), CustomCode.SUCCESS_GET));
  }

  @PatchMapping("/applicant/{applicant_id}")// 지원자의상태를 변경할때 사용하는 API
  @Operation(summary = "지원자 상태 변경", description = "지원자를 상태를 변경한다. ", responses = {
      @ApiResponse(responseCode = "200", description = "지원자 상태 변경 성공", content = @Content(schema = @Schema(implementation = SavedId.class)))
  })
  public ResponseEntity changeApplicantStatus(@Validated @PathVariable(name="applicant_id") Long applicantId, @RequestBody ChangeApplicantStatusRequest applicantStatus){
    SavedId savedId = new SavedId(applicantService.changeApplicantStatus(applicantId,applicantStatus));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.OK.value(), CustomCode.SUCCESS_UPDATE));
  }

  @PostMapping("/applicant")//지원자 생성과 동시에 답변들 저장하는 API
  @Operation(summary = "지원자 생성", description = "지원자를 생성하고 답변을 저장한다.", responses = {
      @ApiResponse(responseCode = "200", description = "지원자 생성 성공", content = @Content(schema = @Schema(implementation = SavedId.class)))
  })
  public ResponseEntity createApplicantAndSaveQuestions(@Validated @RequestBody ApplicantRequest applicantRequest){
    SavedId savedId = new SavedId(applicantService.createApplicant(applicantRequest));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.CREATED.value(), CustomCode.SUCCESS_CREATE));
  }
}
