package com.modong.backend.domain.applicant;

import com.modong.backend.domain.applicant.Dto.ApplicantRequest;
import com.modong.backend.domain.application.Application;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.Enum.ApplicantStatus;
import com.modong.backend.domain.essentialAnswer.EssentialAnswer;
import com.modong.backend.domain.judge.Judge;
import com.modong.backend.domain.questionAnswer.QuestionAnswer;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Applicant extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private float rate;

  @Enumerated(EnumType.STRING)
  private ApplicantStatus applicantStatus;

  private boolean isFail = false;

  @ManyToOne(fetch = FetchType.LAZY)
  private Application application;

  @OneToMany(mappedBy = "applicant")
  private List<EssentialAnswer> essentialAnswers = new ArrayList<>();

  @OneToMany(mappedBy = "applicant")
  private List<QuestionAnswer> questionAnswers = new ArrayList<>();

  @OneToMany(mappedBy = "applicant")
  private List<Judge> judges = new ArrayList<>();

  public Applicant(ApplicantRequest applicantRequest, Application application) {
    this.name = applicantRequest.getName();
    this.application = application;
    this.applicantStatus = ApplicantStatus.ACCEPT;
    this.rate = 0f;
  }

  public void changeStatus(ApplicantStatus applicantStatus) {
    this.applicantStatus = applicantStatus;
  }

  public void fail(){
    this.isFail = true;
  }
}
