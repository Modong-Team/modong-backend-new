package com.modong.backend.auth.member;

import com.modong.backend.Enum.CustomCode;
import com.modong.backend.auth.member.Dto.MemberCheckRequest;
import com.modong.backend.auth.member.Dto.MemberRegisterRequest;
import com.modong.backend.auth.member.Dto.MemberResponse;
import com.modong.backend.base.Dto.BaseResponse;
import com.modong.backend.base.Dto.CheckResponse;
import com.modong.backend.base.Dto.SavedId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name =  "회원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/register")
  @Operation(summary = "회원가입", description = "동아리 코드 및 회원가입에 필요한 데이터들과 함께 회원가입을 한다.", responses = {
      @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SavedId.class)))
  })
  public ResponseEntity saveMember(@RequestBody @Validated MemberRegisterRequest memberRegisterRequest){
    SavedId savedId = new SavedId(memberService.register(memberRegisterRequest));
    return ResponseEntity.created(URI.create("/api/v1/member/" + savedId.getId())).body(new BaseResponse(savedId,HttpStatus.CREATED.value(),CustomCode.SUCCESS_CREATE));
  }

  @GetMapping("/member/{memberId}")
  @Operation(summary = "회원 조회", description = "회원 id로 회원을 조회한다.", responses = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = MemberResponse.class)))
  })
  public ResponseEntity findById(@PathVariable final Long memberId){
    MemberResponse memberResponse = memberService.findById(memberId);
    return ResponseEntity.ok(new BaseResponse(memberResponse, HttpStatus.OK.value(), CustomCode.SUCCESS_GET));
  }

  @PostMapping("/member/check")
  @Operation(summary = "중복 아이디 검사", description = "중복 아이디 검사", responses = {
      @ApiResponse(responseCode = "200", description = "중복 아이디 검사 성공", content = @Content(schema = @Schema(implementation = CheckResponse.class)))
  })
  public ResponseEntity checkMemberId(@RequestBody @Validated MemberCheckRequest memberCheckRequest){
    CheckResponse checkResponse = new CheckResponse(memberService.checkMemberId(memberCheckRequest));
    return ResponseEntity.ok(new BaseResponse(checkResponse, HttpStatus.OK.value(), CustomCode.SUCCESS_CHECK));
  }
}
