package com.modong.backend.Applicant;

import com.modong.backend.Applicant.Dto.ApplicantRequest;
import com.modong.backend.Application.Application;
import com.modong.backend.Application.Dto.ApplicationRequest;
import com.modong.backend.Base.BaseTimeEntity;
import com.modong.backend.Enum.ApplicantStatus;
import com.modong.backend.EssentialAnswer.EssentialAnswer;
import com.modong.backend.QuestionAnswer.QuestionAnswer;
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
