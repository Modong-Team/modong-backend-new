package com.modong.backend.domain.evaluation.dto;

import com.modong.backend.auth.member.Member;
import com.modong.backend.domain.evaluation.Evaluation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "평가 조회")
public class EvaluationResponse {
  @Schema(description = "평가 id", example = "1")
  private Long id;

  @Schema(description = "지원서 id", example = "2")
  private Long applicationId;

  @Schema(description = "지원자 id", example = "3")
  private Long applicantId;
  @Schema(description = "작성자 id", example = "4")
  private Long writerId;
  @Schema(description = "작성자 회원 아이디", example = "test123")
  private String writerMemberId;
  @Schema(description = "작성자 회원 이름", example = "tester")
  private String writerName;
  @Schema(description = "평가 내용", example = "좋아요 !")
  private String comment;
  @Schema(description = "평가 점수", example = "9.6")
  private float score;
  public EvaluationResponse(Evaluation evaluation, Member member, Long applicationId, Long applicantId) {
    this.id = evaluation.getId();
    this.writerId = evaluation.getCreatorId();
    this.writerMemberId = member.getMemberId();
    this.writerName = member.getName();
    this.comment = evaluation.getComment();
    this.applicantId = applicantId;
    this.applicationId = applicationId;
    this.score = evaluation.getScore();
  }
}
