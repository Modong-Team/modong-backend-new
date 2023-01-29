package com.modong.backend.domain.applicant.Dto;

import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.essentialAnswer.Dto.EssentialAnswerResponse;
import com.modong.backend.domain.questionAnswer.Dto.QuestionAnswerResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(name = "지원 상세 조회 응답")
public class ApplicantDetailResponse extends ApplicantSimpleResponse {
  @Schema(description = "필수질단 답변들")
  private List<EssentialAnswerResponse> essentialAnswers = new ArrayList<>();
  @Schema(description = "질문 답변들")
  private List<QuestionAnswerResponse> questionAnswers = new ArrayList<>();

  public ApplicantDetailResponse(Applicant applicant) {
    super(applicant);
    this.essentialAnswers = applicant.getEssentialAnswers().stream().map(EssentialAnswerResponse::new).collect(
        Collectors.toList());
    this.questionAnswers = applicant.getQuestionAnswers().stream().map(QuestionAnswerResponse::new).collect(
        Collectors.toList());
  }
}
