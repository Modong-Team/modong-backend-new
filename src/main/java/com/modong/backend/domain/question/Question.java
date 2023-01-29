package com.modong.backend.domain.question;

import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.Enum.QuestionType;
import com.modong.backend.domain.form.Form;
import com.modong.backend.domain.questionOption.QuestionOption;
import com.modong.backend.domain.question.Dto.QuestionRequest;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private QuestionType questionType;

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private Form form;

  @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  private List<QuestionOption> questionOptions = new ArrayList<>();

  public Question(QuestionRequest questionRequest, Form form) {
    this.questionType = QuestionType.findByCode(questionRequest.getQuestionType());
    this.content = questionRequest.getContent();
    this.form = form;
  }

  public void addOption(QuestionOption questionOption) {
    this.questionOptions.add(questionOption);
  }

  public void setOption(List<QuestionOption> questionOption) {
    this.questionOptions = questionOption;
  }
}