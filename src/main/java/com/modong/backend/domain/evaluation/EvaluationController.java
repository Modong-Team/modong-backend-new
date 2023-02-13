package com.modong.backend.domain.evaluation;

import com.modong.backend.Enum.CustomCode;
import com.modong.backend.auth.support.Auth;
import com.modong.backend.base.Dto.BaseResponse;
import com.modong.backend.base.Dto.ErrorResponse;
import com.modong.backend.base.Dto.SavedId;
import com.modong.backend.domain.evaluation.dto.EvaluationCreateRequest;
import com.modong.backend.domain.evaluation.dto.EvaluationDeleteRequest;
import com.modong.backend.domain.evaluation.dto.EvaluationFindRequest;
import com.modong.backend.domain.evaluation.dto.EvaluationResponse;
import com.modong.backend.domain.evaluation.dto.EvaluationUpdateRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "지원자 평가 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EvaluationController {
  private final EvaluationService evaluationService;

  @PostMapping("/evaluation")//평가 생성 API
  @Operation(summary = "평가 생성", description = "동아리 회원이 지원자를 보고 남긴 평가를 저장한다.", responses = {
      @ApiResponse(responseCode = "201", description = "평가 생성 성공", content = @Content(schema = @Schema(implementation = SavedId.class))),
      @ApiResponse(responseCode = "400", description = "평가 생성 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "평가 생성 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "평가 생성 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity createEvaluation(@Validated @RequestBody EvaluationCreateRequest evaluationCreateRequest, @Auth Long memberId){
    SavedId savedId = new SavedId(evaluationService.create(evaluationCreateRequest,memberId));
    return ResponseEntity.created(
        URI.create("/api/v1/evaluation/" + savedId.getId())).body(new BaseResponse(savedId, HttpStatus.CREATED.value(),
        CustomCode.SUCCESS_CREATE));
  }
  @PutMapping("/evaluation")//평가 수정 API
  @Operation(summary = "평가 수정", description = "동아리 회원이 지원자를 보고 남긴 평가를 저장한다.", responses = {
      @ApiResponse(responseCode = "200", description = "평가 수정 성공", content = @Content(schema = @Schema(implementation = SavedId.class))),
      @ApiResponse(responseCode = "400", description = "평가 수정 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "평가 수정 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "평가 수정 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity updateEvaluation(@Validated @RequestBody EvaluationUpdateRequest evaluationUpdateRequest, @Auth Long memberId){
    SavedId savedId = new SavedId(evaluationService.update(evaluationUpdateRequest,memberId));
    return ResponseEntity.ok(new BaseResponse(savedId,HttpStatus.OK.value(),CustomCode.SUCCESS_UPDATE));
  }
  @DeleteMapping("/evaluation")//평가 삭제 API
  @Operation(summary = "평가 삭제", description = "동아리 회원이 지원자를 보고 남긴 평가를 저장한다.", responses = {
      @ApiResponse(responseCode = "200", description = "평가 삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
      @ApiResponse(responseCode = "400", description = "평가 삭제 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "평가 삭제 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "평가 삭제 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity deleteEvaluation(@Validated @RequestBody EvaluationDeleteRequest evaluationDeleteRequest, @Auth Long memberId){
    evaluationService.delete(evaluationDeleteRequest,memberId);
    return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),CustomCode.SUCCESS_DELETE));
  }

  @GetMapping("/evaluations")//평가 조회 API
  @Operation(summary = "지원자에 대한 전체 평가 조회", description = "동아리 회원이 지원자를 보고 남긴 평가를 저장한다.", responses = {
      @ApiResponse(responseCode = "200", description = "평가 조회 성공", content = @Content(schema = @Schema(implementation = EvaluationResponse.class))),
      @ApiResponse(responseCode = "400", description = "평가 조회 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "평가 조회 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "평가 조회 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity getEvaluationsByApplicant(@Validated EvaluationFindRequest evaluationFindRequest, @Auth Long memberId){
    List<EvaluationResponse> evaluations = evaluationService.findAllByApplication(evaluationFindRequest,memberId);
    return ResponseEntity.ok(new BaseResponse(evaluations,HttpStatus.OK.value(),CustomCode.SUCCESS_GET_LIST));
  }

}
