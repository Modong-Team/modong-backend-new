package com.modong.backend.domain.form;

import com.modong.backend.auth.support.Auth;
import com.modong.backend.base.Dto.BaseResponse;
import com.modong.backend.base.Dto.SavedId;
import com.modong.backend.Enum.CustomCode;
import com.modong.backend.domain.form.dto.FormRequest;
import com.modong.backend.domain.form.dto.FormResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name =  "지원서 페이지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FormController {

  private final FormService formService;
  //페이지 생성
  @PostMapping("/applications/{application_id}/forms")
  @Operation(summary = "페이지 생성", description = "지원서에 새로운 페이지를 저장한다. 질문 유형 QUESTION(1), SINGLE_SELECT_QUESTION(2), MULTI_SELECT_QUESTION(3)", responses = {
      @ApiResponse(responseCode = "201", description = "질문 페이지 생성 성공", content = @Content(schema = @Schema(implementation = SavedId.class)))
  })
  public ResponseEntity createForm(@Valid @RequestBody FormRequest formRequest, @Auth Long memberId, @Valid @PathVariable(name = "application_id") Long applicationId){
      SavedId savedId = new SavedId(formService.create(formRequest, memberId, applicationId));
      return ResponseEntity.created(URI.create("/api/v1/form/" + savedId.getId())).body(new BaseResponse(savedId, HttpStatus.CREATED.value(), CustomCode.SUCCESS_CREATE));
  }
  //페이지 수정
  @PutMapping("/applications/{application_id}/forms/{form_id}")
  @Operation(summary = "페이지 수정", description = "페이지의 ID를 이용해 수정한다. 질문 유형 QUESTION(1), SINGLE_SELECT_QUESTION(2), MULTI_SELECT_QUESTION(3)", responses = {
      @ApiResponse(responseCode = "200", description = "페이지 수정 성공", content = @Content(schema = @Schema(implementation = SavedId.class)))
  })
  public ResponseEntity updateForm(@Valid @PathVariable(name = "form_id") Long formId, @RequestBody FormRequest formRequest, @Auth Long memberId, @Valid @PathVariable(name = "application_id") Long applicationId){
    SavedId savedId = new SavedId(formService.update(formId,formRequest, memberId));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.OK.value(), CustomCode.SUCCESS_UPDATE));
  }


  //id로 페이지 조회
  @GetMapping("/applications/{application_id}/forms/{form_id}")
  @Operation(summary = "페이지 조회", description = "페이지의 ID를 이용해 조회한다.", responses = {
      @ApiResponse(responseCode = "200", description = "페이지 조회 성공", content = @Content(schema = @Schema(implementation = FormResponse.class)))
  })
  public ResponseEntity getFormById(@Valid @PathVariable(name = "form_id") Long formId){

    FormResponse form = formService.findById(formId);
    return ResponseEntity.ok(new BaseResponse(form,HttpStatus.OK.value(), CustomCode.SUCCESS_GET));
  }

  //지원서 id로 해당되는 모든 페이지 조회
  @GetMapping("applications/{application_id}/forms")
  @Operation(summary = "지원서 ID 로 해당되는 모든 페이지 조회", description = "지원서 ID를 이용해 포함된 모든 페이지를 조회한다.",responses = {
      @ApiResponse(responseCode = "200", description = "페이지 리스트 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FormResponse.class))))
  })
  public ResponseEntity getFormsByApplicationId(@Valid @PathVariable(name = "application_id") Long applicationId){
    List<FormResponse> forms = formService.findAllByApplicationId(applicationId);
    return ResponseEntity.ok(new BaseResponse(forms, HttpStatus.OK.value(), CustomCode.SUCCESS_GET_LIST));
  }

  //지원서의 페이지 삭제
  @DeleteMapping("applications/{application_id}/forms/{form_id}")
  @Operation(summary = "페이지 삭제", description = "페이지의 ID를 이용해 삭제한다.", responses = {
      @ApiResponse(responseCode = "200", description = "페이지 삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
  })
  public ResponseEntity deleteFormById(@Valid @PathVariable(name = "form_id") Long formId, @Auth Long memberId){
    formService.deleteForm(formId,memberId);
    return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(), CustomCode.SUCCESS_DELETE));
  }

}
