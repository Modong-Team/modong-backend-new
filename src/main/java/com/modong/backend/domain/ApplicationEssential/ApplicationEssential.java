package com.modong.backend.domain.ApplicationEssential;

import com.modong.backend.domain.Application.Application;
import com.modong.backend.Base.BaseTimeEntity;
import com.modong.backend.domain.EssentialQuestion.EssentialQuestion;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplicationEssential extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private EssentialQuestion essentialQuestion;

  @ManyToOne(fetch = FetchType.LAZY)
  private Application application;

  public ApplicationEssential(Application application, EssentialQuestion essentialQuestion) {
    this.application = application;
    this.essentialQuestion = essentialQuestion;
  }
}
