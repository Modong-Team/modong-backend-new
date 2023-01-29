package com.modong.backend.domain.questionAnswer;

import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.question.Question;
import com.modong.backend.domain.questionAnswer.Dto.QuestionAnswerRequest;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionAnswer extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  private String answer;

  @ManyToOne(fetch = FetchType.LAZY)
  private Question question;

  @ManyToOne(fetch = FetchType.LAZY)
  private Applicant applicant;


  public QuestionAnswer(QuestionAnswerRequest questionAnswerRequest, Question question, Applicant applicant) {
    this.answer = questionAnswerRequest.getAnswer();
    this.question = question;
    this.applicant = applicant;
  }
}