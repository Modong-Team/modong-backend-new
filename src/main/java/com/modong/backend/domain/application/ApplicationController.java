package com.modong.backend.domain.application;

import com.modong.backend.base.Dto.BaseResponse;
import com.modong.backend.base.Dto.SavedId;
import com.modong.backend.Enum.CustomCode;
import com.modong.backend.domain.application.Dto.ApplicationCreateRequest;
import com.modong.backend.domain.application.Dto.ApplicationDetailResponse;
import com.modong.backend.domain.application.Dto.ApplicationSimpleResponse;
import com.modong.backend.domain.application.Dto.ApplicationUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "지원서 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApplicationController {
  private final ApplicationService applicationService;

  //지원서 생성
  @PostMapping("/application")
  @Operation(summary = "지원서를 생성한다.", description = "지원서를 생성한다.", responses = {
      @ApiResponse(responseCode = "201", description = "지원서 조회 성공", content = @Content(schema = @Schema(implementation = ApplicationDetailResponse.class)))
  })
  public ResponseEntity createApplication(@Validated @RequestBody ApplicationCreateRequest applicationCreateRequest){
    SavedId savedId = new SavedId(applicationService.createApplication(applicationCreateRequest));
    return ResponseEntity.created(URI.create("/api/v1/application/" + savedId.getId())).body(new BaseResponse(savedId,HttpStatus.CREATED.value(),CustomCode.SUCCESS_CREATE));
  }

  //지원서 수정(필수 질문 부분)
  @PutMapping("/application/{application_id}")
  @Operation(summary = "지원서 수정", description = "지원서의 ID를 이용해 작성한 지원서를 수정한다.")
  public ResponseEntity updateApplication(@Validated @PathVariable(name = "application_id") Long applicationId, @RequestBody ApplicationUpdateRequest applicationUpdateRequest){
    SavedId savedId = new SavedId(applicationService.updateApplication(applicationId, applicationUpdateRequest));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.OK.value(), CustomCode.SUCCESS_UPDATE));

  }

  //지원서 삭제
  @DeleteMapping("/application/{application_id}")
  @Operation(summary = "지원서 삭제", description = "지원서의 ID를 이용해 작성한 지원서를 삭제한다.")
  public ResponseEntity deleteApplicationById(@Validated @PathVariable(name = "application_id") Long applicationId) {
    applicationService.deleteApplication(applicationId);
    return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), CustomCode.SUCCESS_DELETE));
  }

  @PatchMapping("/application/open/{application_id}")
  @Operation(summary = "지원서 모집으로 상태 변경", description = "지원서의 ID를 이용해 작성한 지원서의 상태를 모집으로 수정한다.")
  public ResponseEntity updateStatusToOpen(@Validated @PathVariable(name = "application_id") Long applicationId){
    SavedId savedId = new SavedId(applicationService.open(applicationId));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.OK.value(), CustomCode.SUCCESS_UPDATE));
  }
  @PatchMapping("/application/close/{application_id}")
  @Operation(summary = "지원서 마감으로 상태 변경", description = "지원서의 ID를 이용해 작성한 지원서의 상태를 마감으로 수정한다.")
  public ResponseEntity updateStatusToClose(@Validated @PathVariable(name = "application_id") Long applicationId){
    SavedId savedId = new SavedId(applicationService.close(applicationId));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.OK.value(), CustomCode.SUCCESS_UPDATE));
  }
  //동아리 전체 지원서 조회
  @GetMapping("/applications/{club_id}")
  @Operation(summary = "동아리의 전체 지원서 조회", description = "동아리의 ID를 이용해 작성한 모든 지원서를 조회한다.", responses = {
      @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ApplicationSimpleResponse.class))))
  })
  public ResponseEntity getApplicationsByClubId(@Validated @PathVariable(name="club_id") Long clubId){
    List<ApplicationSimpleResponse> applications = applicationService.findAllByClubId(clubId);
    return ResponseEntity.ok(new BaseResponse(applications, HttpStatus.OK.value(), CustomCode.SUCCESS_GET_LIST));
  }
  //지원서 조회
  @GetMapping("/application/{application_id}")
  @Operation(summary = "지원서 조회", description = "지원서의 ID를 이용해 작성한 지원서를 조회한다.", responses = {
      @ApiResponse(responseCode = "200", description = "지원서 조회 성공", content = @Content(schema = @Schema(implementation = ApplicationDetailResponse.class)))
  })
  public ResponseEntity getApplicationById(@Validated @PathVariable(name = "application_id") Long applicationId){
    ApplicationDetailResponse application = applicationService.findDetailById(applicationId);
    return ResponseEntity.ok(new BaseResponse(application,HttpStatus.OK.value(), CustomCode.SUCCESS_GET));
  }
  //지원서 조회(
  @GetMapping("/view/application/{url_id}")
  @Operation(summary = "지원서 조회", description = "지원서의 링크 아이디를 이용해 작성한 지원서를 조회한다.", responses = {
      @ApiResponse(responseCode = "200", description = "지원서 조회 성공", content = @Content(schema = @Schema(implementation = ApplicationDetailResponse.class)))
  })
  public ResponseEntity getApplicationByUrlId(@Validated @PathVariable(name = "url_id") String urlId){
    ApplicationDetailResponse application = applicationService.findDetailByUrlId(urlId);
    return ResponseEntity.ok(new BaseResponse(application,HttpStatus.OK.value(), CustomCode.SUCCESS_GET));
  }

}
