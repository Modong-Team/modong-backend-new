package com.modong.backend.base;
import com.modong.backend.Enum.CustomCode;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.base.Dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "기본 API")
@RestController
public class BaseController {

  @GetMapping("/")
  @Operation(summary = "헬스체크용", responses = {
      @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
  })
  public BaseResponse running() {
    return new BaseResponse("Server is running!!", HttpStatus.OK.value(), CustomCode.SUCCESS_GET);
  }
}
