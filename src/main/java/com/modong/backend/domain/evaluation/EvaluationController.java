package com.modong.backend.domain.evaluation;

import com.modong.backend.Enum.CustomCode;
import com.modong.backend.auth.support.Auth;
import com.modong.backend.base.Dto.*;
import com.modong.backend.domain.evaluation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "지원자 평가 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EvaluationController {
  private final EvaluationService evaluationService;

  @PostMapping("/applicants/{applicant_id}/evaluations")//지원자에 대한 평가 생성 API
  @Operation(summary = "지원자에 대한 평가 생성", description = "회원이 지원자를 보고 남긴 평가를 저장한다.", responses = {
      @ApiResponse(responseCode = "201", description = "평가 생성 성공", content = @Content(schema = @Schema(implementation = SavedId.class))),
      @ApiResponse(responseCode = "400", description = "평가 생성 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "평가 생성 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "평가 생성 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity createEvaluation(@Validated @RequestBody EvaluationCreateRequest evaluationCreateRequest, @Auth Long memberId, @PathVariable(name = "applicant_id") Long applicantId){
    SavedId savedId = new SavedId(evaluationService.create(evaluationCreateRequest,memberId,applicantId));
    return ResponseEntity.created(
        URI.create("/api/v1/applicants/"+ applicantId + "/evaluations/" + savedId.getId())).body(new BaseResponse(savedId, HttpStatus.CREATED.value(),
        CustomCode.SUCCESS_CREATE));
  }
  @PutMapping("/applicants/{applicant_id}/evaluations/{evaluation_id}")//지원자에 대한 평가 수정 API
  @Operation(summary = "지원자에 대한 평가 수정", description = "회원이 지원자를 보고 남긴 평가를 수정한다.", responses = {
      @ApiResponse(responseCode = "200", description = "평가 수정 성공", content = @Content(schema = @Schema(implementation = SavedId.class))),
      @ApiResponse(responseCode = "400", description = "평가 수정 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "평가 수정 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "평가 수정 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity updateEvaluation(@Validated @RequestBody EvaluationUpdateRequest evaluationUpdateRequest, @Validated @PathVariable(name = "evaluation_id") Long evaluationId, @Auth Long memberId, @PathVariable(name = "applicant_id") Long applicantId){
    SavedId savedId = new SavedId(evaluationService.update(evaluationUpdateRequest,evaluationId,memberId));
    return ResponseEntity.ok(new BaseResponse(savedId,HttpStatus.OK.value(),CustomCode.SUCCESS_UPDATE));
  }
  @DeleteMapping("/applicants/{applicant_id}/evaluations/{evaluation_id}")//지원자에 대한 평가 삭제 API
  @Operation(summary = "지원자에 대한 평가 삭제", description = "회원이 지원자를 보고 남긴 평가를 삭제한다.", responses = {
      @ApiResponse(responseCode = "200", description = "평가 삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
      @ApiResponse(responseCode = "400", description = "평가 삭제 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "평가 삭제 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "평가 삭제 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity deleteEvaluation(@Validated @PathVariable(name = "evaluation_id") Long evaluationId, @Auth Long memberId, @PathVariable(name = "applicant_id") Long applicantId){
    evaluationService.delete(evaluationId,memberId);
    return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),CustomCode.SUCCESS_DELETE));
  }

  @GetMapping("/applicants/{applicant_id}/evaluations")//지원자에 대한 모든 평가 조회 API
  @Operation(summary = "지원자에 대한 전체 평가 조회", description = "동아리 회원들이 지원자에게 남긴 평가들을 조회한다.", responses = {
      @ApiResponse(responseCode = "200", description = "평가 조회 성공", content = @Content(schema = @Schema(implementation = EvaluationResponse.class))),
      @ApiResponse(responseCode = "400", description = "평가 조회 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "평가 조회 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "평가 조회 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity getEvaluationsByApplicant(@Validated @PathVariable(name = "applicant_id") Long applicantId, @Auth Long memberId){
    List<EvaluationResponse> evaluations = evaluationService.findAllByApplication(applicantId,memberId);
    return ResponseEntity.ok(new BaseResponse(evaluations,HttpStatus.OK.value(),CustomCode.SUCCESS_GET_LIST));
  }

  @GetMapping("/applicants/{applicant_id}/evaluations/{evaluation_id}")//지원자에 대한 평가 조회 API
  @Operation(summary = "지원자에 대한 평가 조회", description = "id로 평가한 내용을 조회한다.", responses = {
          @ApiResponse(responseCode = "200", description = "평가 조회 성공", content = @Content(schema = @Schema(implementation = EvaluationResponse.class))),
          @ApiResponse(responseCode = "400", description = "평가 조회 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "401", description = "평가 조회 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "404", description = "평가 조회 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity getEvaluationById(@Validated @PathVariable(name = "evaluation_id") Long evaluationId, @Auth Long memberId, @PathVariable(name = "applicant_id") Long applicantId){
    EvaluationResponse evaluation = evaluationService.findById(evaluationId,memberId);
    return ResponseEntity.ok(new BaseResponse(evaluation,HttpStatus.OK.value(),CustomCode.SUCCESS_GET));
  }
  @GetMapping("/applicants/{applicant_id}/member/evaluations")//지원자에 대한 회원의 평가 조회
  @Operation(summary = "지원자에 대한 평가 조회", description = "내가 지원자에게 평가한 내용을 조회한다.", responses = {
          @ApiResponse(responseCode = "200", description = "평가 조회 성공", content = @Content(schema = @Schema(implementation = EvaluationResponse.class))),
          @ApiResponse(responseCode = "400", description = "평가 조회 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "401", description = "평가 조회 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "404", description = "평가 조회 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity getEvaluationByApplicantAndMember(@Validated @PathVariable(name = "applicant_id") Long applicantId, @Auth Long memberId){
    EvaluationResponse evaluation = evaluationService.findByApplicantIdAndMemberId(applicantId,memberId);
    return ResponseEntity.ok(new BaseResponse(evaluation,HttpStatus.OK.value(),CustomCode.SUCCESS_GET));
  }
  @GetMapping("/applicants/{applicant_id}/exist/evaluations")//지원자에 대한 평가 존재 검사 API
  @Operation(summary = "지원자에 대한 평가 존재 검사 요청", description = "회원이 지원자를 평가를 한 적이 있는지 검사한다.", responses = {
          @ApiResponse(responseCode = "200", description = "평가 존재 검사 요청 성공", content = @Content(schema = @Schema(implementation = EvaluationResponse.class))),
          @ApiResponse(responseCode = "400", description = "평가 존재 검사 요청 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "401", description = "평가 존재 검사 요청 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "404", description = "평가 존재 검사 요청 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity checkEvaluationIfExists(@Validated @PathVariable(name = "applicant_id") Long applicantId, @Auth Long memberId){
    ExistsResponse existsResponse = new ExistsResponse(evaluationService.check(applicantId, memberId));
    return ResponseEntity.ok(new BaseResponse(existsResponse,HttpStatus.OK.value(),CustomCode.SUCCESS_EXISTS_CHECK));
  }
}
