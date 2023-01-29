package com.modong.backend.domain.form.dto;

import com.modong.backend.domain.form.Form;
import com.modong.backend.domain.question.Dto.QuestionResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(name = "지원서 페이지 응답")
public class FormResponse {

  @Schema(description = "지원서 id",  example = "1")
  private Long id;
  @Schema(description = "지원서 제목")
  private String title;
  @Schema(description = "질문 리스트")
  private List<QuestionResponse> questions = new ArrayList<>();

  public FormResponse(Form form) {
    this.id = form.getId();
    this.title = form.getTitle();
    this.questions = form.getQuestions().stream().map(QuestionResponse::new).collect(Collectors.toList());
  }
  
}
