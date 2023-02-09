package com.modong.backend.domain.memo.dto;

import com.modong.backend.auth.member.Member;
import com.modong.backend.domain.application.Application;
import com.modong.backend.domain.memo.Memo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "메모 조회")
public class MemoResponse {
  @Schema(description = "메모 id", example = "1")
  private Long id;

  @Schema(description = "지원서 id", example = "2")
  private Long applicationId;

  @Schema(description = "지원자 id", example = "3")
  private Long applicantId;
  @Schema(description = "작성자 id", example = "4")
  private Long creatorId;
  @Schema(description = "작성자 회원 아이디", example = "test123")
  private String writerId;
  @Schema(description = "작성자 회원 이름", example = "tester")
  private String writerName;
  @Schema(description = "메모 내용", example = "면접 질문 : 나이가 어떻게 되시나요?")
  private String content;
  public MemoResponse(Memo memo, Member member, Long applicationId, Long applicantId) {
    this.id = memo.getId();
    this.creatorId = memo.getCreatorId();
    this.writerId = member.getMemberId();
    this.writerName = member.getName();
    this.content = memo.getContent();
    this.applicantId = applicantId;
    this.applicationId = applicationId;
  }
}
