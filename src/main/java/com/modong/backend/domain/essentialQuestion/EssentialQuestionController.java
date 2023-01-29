package com.modong.backend.domain.essentialQuestion;

import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.base.Dto.BaseResponse;
import com.modong.backend.Enum.CustomCode;
import com.modong.backend.domain.essentialQuestion.Dto.EssentialQuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "필수질문 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EssentialQuestionController {

  private final EssentialQuestionService essentialQuestionService;

  //필수 질문 전체 조회

  @GetMapping("/essentials")
  @Operation(summary = "필수 질문 전체 조회", description = "필수로 등록된 모든 질문을 조회한다.", responses = {
      @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EssentialQuestionResponse.class))))
  })
  public ResponseEntity getAllEssentialQuestions(){
    List<EssentialQuestionResponse> essentialQuestions = essentialQuestionService.findAll();

    return ResponseEntity.ok(new BaseResponse(essentialQuestions, HttpStatus.OK.value(), CustomCode.SUCCESS_GET_LIST));
  }

}
