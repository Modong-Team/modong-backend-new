package com.modong.backend.domain.form.dto;

import com.modong.backend.domain.question.Dto.QuestionRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Schema(name = "지원서 페이지 생성 요청")
@NoArgsConstructor
public class FormRequest {

  @NotNull
  @Schema(description = "지원서 ID",  example = "1")
  private Long applicationId;

  @NotBlank
  @Schema(description = "페이지 제목",  example = "첫번째 페이지")
  private String title;

  @Schema(description = "페이지에 들어갈 질문들", nullable = true)
  private List<QuestionRequest> questionRequests = new ArrayList<>();

}
