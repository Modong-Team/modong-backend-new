package com.modong.backend.domain.applicant.Dto;

import com.modong.backend.domain.essentialAnswer.Dto.EssentialAnswerRequest;
import com.modong.backend.domain.questionAnswer.Dto.QuestionAnswerRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(name = "지원자 생성 요청")
@NoArgsConstructor
public class ApplicantCreateRequest {

  @NotNull
  @Schema(description = "지원서 ID",  example = "1")
  private Long applicationId;

  @NotBlank
  @Schema(description = "지원자 이름",  example = "홍길동")
  private String name;

  @NotEmpty(message = "Input essential Answer list cannot be empty.")
  @Schema(description = "필수 질문 답변들")
  private List<EssentialAnswerRequest> essentialAnswers = new ArrayList<>();

  @Schema(description = "질문 답변들")
  private List<QuestionAnswerRequest> questionAnswers = new ArrayList<>();


  @Builder
  public ApplicantCreateRequest(Long applicationId, String name,
      List<EssentialAnswerRequest> essentialAnswers, List<QuestionAnswerRequest> questionAnswers) {
    this.applicationId = applicationId;
    this.name = name;
    this.essentialAnswers = essentialAnswers;
    this.questionAnswers = questionAnswers;
  }
}
