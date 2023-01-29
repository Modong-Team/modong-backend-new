package com.modong.backend.auth;

import com.modong.backend.Enum.CustomCode;
import com.modong.backend.auth.Dto.LoginRequest;
import com.modong.backend.auth.Dto.TokenRequest;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.base.Dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name =  "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  @Operation(summary = "로그인", description = "아이디와 비밀번호를 사용해 로그인을 한다.",
      responses = {@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenResponse.class)))}
  )
  public ResponseEntity login(@Validated @RequestBody LoginRequest loginRequest){
    TokenResponse token = authService.login(loginRequest);
    return ResponseEntity.ok(new BaseResponse(token, HttpStatus.OK.value(), CustomCode.SUCCESS_LOGIN));
  }

  @PostMapping("/token")
  @Operation(summary = "토큰 재발행", description = "리프레시 토큰을 이용해 액세스 토큰을 재발행 한다. 이때 리프레시 토큰도 바뀐다.",
      responses = {@ApiResponse(responseCode = "200", description = "토큰 재발행 성공", content = @Content(schema = @Schema(implementation = TokenResponse.class)))}
  )
  public ResponseEntity getToken(@Validated @RequestBody TokenRequest tokenRequest) {
    TokenResponse token = authService.createAccessToken(tokenRequest);
    return ResponseEntity.ok(new BaseResponse(token, HttpStatus.OK.value(), CustomCode.SUCCESS_GET_TOKEN));
  }
}
