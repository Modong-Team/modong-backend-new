package com.modong.backend.domain.essentialAnswer;

import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.essentialAnswer.Dto.EssentialAnswerRequest;
import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
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
public class EssentialAnswer extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  private String answer;

  @ManyToOne(fetch = FetchType.LAZY)
  private EssentialQuestion essentialQuestion;
  @ManyToOne(fetch = FetchType.LAZY)
  private Applicant applicant;


  // 뭔가 Request 객체를 받아서 하고 싶지만 안에 쓰는 놈은 하난데??? 하지만 나중을 위해서 그렇게 쓰자 ㅋ
  public EssentialAnswer(EssentialAnswerRequest essentialAnswerRequest, EssentialQuestion essentialQuestion, Applicant applicant) {
    this.answer = essentialAnswerRequest.getAnswer();
    this.essentialQuestion = essentialQuestion;
    this.applicant = applicant;
  }
}
