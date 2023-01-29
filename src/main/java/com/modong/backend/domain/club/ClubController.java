package com.modong.backend.domain.club;

import com.modong.backend.Enum.CustomCode;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.base.Dto.BaseResponse;
import com.modong.backend.domain.club.Dto.ClubCreateResponse;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.domain.club.Dto.ClubResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "동아리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ClubController {

  private final ClubService clubService;

  //동아리 생성에 관한 api
  @PostMapping("/club")
  @Operation(summary = "동아리 생성", description = "동아리를 생성한다.", responses = {
      @ApiResponse(responseCode = "200", description = "동아리 생성 성공", content = @Content(schema = @Schema(implementation = ClubCreateResponse.class)))
  })
  public ResponseEntity saveClub(@RequestBody @Valid ClubCreateRequest clubCreateRequest){
    ClubCreateResponse club = clubService.save(clubCreateRequest);
    return ResponseEntity.created(URI.create("/api/v1/register")).body(new BaseResponse(club, HttpStatus.CREATED.value(), CustomCode.SUCCESS_CREATE));
  }

  @GetMapping("/club/{clubId}")
  @Operation(summary = "동아리 조회", description = "동아리를 조회한다.", responses = {
      @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = ClubResponse.class)))
  })
  public ResponseEntity getClubById(@PathVariable final Long clubId){
    ClubResponse club = clubService.findById(clubId);
    return ResponseEntity.ok(new BaseResponse(club,HttpStatus.OK.value(), CustomCode.SUCCESS_GET));
  }

}
