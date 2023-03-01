package com.modong.backend.domain.memo;

import com.modong.backend.Enum.CustomCode;
import com.modong.backend.auth.support.Auth;
import com.modong.backend.base.Dto.BaseResponse;
import com.modong.backend.base.Dto.ErrorResponse;
import com.modong.backend.base.Dto.SavedId;
import com.modong.backend.domain.memo.dto.MemoCreateRequest;
import com.modong.backend.domain.memo.dto.MemoFindRequest;
import com.modong.backend.domain.memo.dto.MemoResponse;
import com.modong.backend.domain.memo.dto.MemoUpdateRequest;
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

@Tag(name = "지원자 메모 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemoController {

  private final MemoService memoService;

  @PostMapping("/memo")//메모 생성 API
  @Operation(summary = "메모 생성", description = "동아리 회원이 지원자를 보고 남긴 메모를 저장한다.", responses = {
      @ApiResponse(responseCode = "201", description = "메모 생성 성공", content = @Content(schema = @Schema(implementation = SavedId.class))),
      @ApiResponse(responseCode = "400", description = "메모 생성 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "메모 생성 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "메모 생성 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity createMemo(@Validated @RequestBody MemoCreateRequest memoCreateRequest, @Auth Long memberId){
    SavedId savedId = new SavedId(memoService.create(memoCreateRequest,memberId));
    return ResponseEntity.created(URI.create("/api/v1/memo/" + savedId.getId())).body(new BaseResponse(savedId,HttpStatus.CREATED.value(),CustomCode.SUCCESS_CREATE));
  }
  @PutMapping("/memo/{memo_id}")//메모 수정 API
  @Operation(summary = "메모 수정", description = "동아리 회원이 지원자를 보고 남긴 메모를 저장한다.", responses = {
      @ApiResponse(responseCode = "200", description = "메모 수정 성공", content = @Content(schema = @Schema(implementation = SavedId.class))),
      @ApiResponse(responseCode = "400", description = "메모 수정 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "메모 수정 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "메모 수정 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity updateMemo(@Validated @RequestBody MemoUpdateRequest memoUpdateRequest, @Validated @PathVariable(name = "memo_id") Long memoId, @Auth Long memberId){
    SavedId savedId = new SavedId(memoService.update(memoUpdateRequest,memoId,memberId));
    return ResponseEntity.ok(new BaseResponse(savedId,HttpStatus.OK.value(),CustomCode.SUCCESS_UPDATE));
  }
  @DeleteMapping("/memo/{memo_id}")//메모 삭제 API
  @Operation(summary = "메모 삭제", description = "동아리 회원이 지원자를 보고 남긴 메모를 저장한다.", responses = {
      @ApiResponse(responseCode = "200", description = "메모 삭제 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
      @ApiResponse(responseCode = "400", description = "메모 삭제 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "메모 삭제 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "메모 삭제 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity deleteMemo(@Validated @PathVariable(name = "memo_id") Long memoId, @Auth Long memberId){
    memoService.delete(memoId,memberId);
    return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.value(),CustomCode.SUCCESS_DELETE));
  }

  @GetMapping("/memos")//메모 조회 API
  @Operation(summary = "지원자에 대한 전체 메모 조회", description = "동아리 회원이 지원자를 보고 남긴 메모를 저장한다.", responses = {
      @ApiResponse(responseCode = "200", description = "메모 조회 성공", content = @Content(schema = @Schema(implementation = MemoResponse.class))),
      @ApiResponse(responseCode = "400", description = "메모 조회 실패 - 잘못된 인자, 유효하지 않은 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "401", description = "메모 조회 실패 - 권한 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "메모 조회 실패 - 요청으로 받은 리소스를 찾을 수 없음 ", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity getMemosByApplicant(@Validated MemoFindRequest memoFindRequest, @Auth Long memberId){
    List<MemoResponse> memos = memoService.findAllByApplication(memoFindRequest,memberId);
    return ResponseEntity.ok(new BaseResponse(memos,HttpStatus.OK.value(),CustomCode.SUCCESS_GET_LIST));
  }

}
