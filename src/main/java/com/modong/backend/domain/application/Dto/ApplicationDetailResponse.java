package com.modong.backend.domain.application.Dto;

import com.modong.backend.domain.application.Application;
import com.modong.backend.domain.essentialQuestion.Dto.EssentialQuestionResponse;
import com.modong.backend.domain.form.dto.FormResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(name = "지원서 상세 조회")
public class ApplicationDetailResponse extends ApplicationSimpleResponse{
  @Schema(description = "지원서 필수 질문 리스트")
  private List<EssentialQuestionResponse> essentialQuestions = new ArrayList<>();
  @Schema(description = "지원서 질문 페이지 리스트")
  private List<FormResponse> forms = new ArrayList<>();

  public ApplicationDetailResponse(Application application) {
    super(application);
  }
  public void addEssentialQuestion(EssentialQuestionResponse essentialQuestionResponse){
    this.essentialQuestions.add(essentialQuestionResponse);
  }
  public void addForm(FormResponse formResponse){
    this.forms.add(formResponse);
  }


}
