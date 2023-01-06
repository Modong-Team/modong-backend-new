package com.modong.backend.EssentialQuestion;

import com.modong.backend.Base.Dto.BaseResponse;
import com.modong.backend.Enum.MessageCode;
import com.modong.backend.EssentialQuestion.Dto.EssentialQuestionResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Api(tags = "필수질문 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EssentialQuestionController {

  private final EssentialQuestionService essentialQuestionService;

  //필수 질문 전체 조회

  @GetMapping("/essentials")
  @Operation(summary = "필수 질문 전체 조회", description = "필수로 등록된 모든 질문을 조회한다.")
  public ResponseEntity getAllEssentialQuestions(){
    List<EssentialQuestionResponse> essentialQuestions = essentialQuestionService.findAll();

    return ResponseEntity.ok(new BaseResponse(essentialQuestions, HttpStatus.OK.value(), MessageCode.SUCCESS_GET_LIST));
  }

}
