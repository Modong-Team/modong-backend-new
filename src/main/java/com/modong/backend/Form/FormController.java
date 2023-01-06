package com.modong.backend.Form;

import com.modong.backend.Base.Dto.BaseResponse;
import com.modong.backend.Base.Dto.SavedId;
import com.modong.backend.Enum.MessageCode;
import com.modong.backend.Form.dto.FormRequest;
import com.modong.backend.Form.dto.FormResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Api(tags =  "지원서 페이지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FormController {

  private final FormService formService;
  //페이지 생성
  @PostMapping("/form")
  @Operation(summary = "페이지 생성", description = "지원서에 새로운 페이지를 저장한다. 질문 유형 QUESTION(1), SINGLE_SELECT_QUESTION(2), MULTI_SELECT_QUESTION(3)")
  public ResponseEntity createForm(@Valid @RequestBody FormRequest formRequest){
      SavedId savedId = new SavedId(formService.create(formRequest));
      return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.CREATED.value(), MessageCode.SUCCESS_CREATE));
  }
  //페이지 수정
  @PutMapping("/form/{form_id}")
  @Operation(summary = "페이지 수정", description = "페이지의 ID를 이용해 수정한다. 질문 유형 QUESTION(1), SINGLE_SELECT_QUESTION(2), MULTI_SELECT_QUESTION(3)")
  public ResponseEntity updateForm(@Valid @PathVariable(name = "form_id") Long formId, @RequestBody FormRequest formRequest){
    SavedId savedId = new SavedId(formService.update(formId,formRequest));
    return ResponseEntity.ok(new BaseResponse(savedId, HttpStatus.OK.value(), MessageCode.SUCCESS_UPDATE));
  }


  //id로 페이지 조회
  @GetMapping("/form/{form_id}")
  @Operation(summary = "페이지 조회", description = "페이지의 ID를 이용해 조회한다.")
  public ResponseEntity getFormById(@Valid @PathVariable(name = "form_id") Long formId){

    FormResponse form = formService.findById(formId);
    return ResponseEntity.ok(new BaseResponse(form,HttpStatus.OK.value(),MessageCode.SUCCESS_GET));
  }

  //지원서 id로 해당되는 모든 페이지 조회
  @GetMapping("/forms/{application_id}")
  @Operation(summary = "지원서 ID 로 해당되는 모든 페이지 조회", description = "지원서 ID를 이용해 포함된 모든 페이지를 조회한다.")
  public ResponseEntity getFormsByApplicationId(@Valid @PathVariable(name = "application_id") Long applicationId){
    List<FormResponse> forms = formService.findAllByApplicationId(applicationId);
    return ResponseEntity.ok(new BaseResponse(forms, HttpStatus.OK.value(), MessageCode.SUCCESS_GET_LIST));
  }

  //지원서의 페이지 삭제
  @DeleteMapping("/form/{form_id}")
  @Operation(summary = "페이지 삭제", description = "페이지의 ID를 이용해 삭제한다.")
  public ResponseEntity deleteFormById(@Valid @PathVariable(name = "form_id") Long formId){
    formService.deleteForm(formId);
    return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),MessageCode.SUCCESS_DELETE));
  }

}
