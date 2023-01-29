package com.modong.backend.domain.applicationEssential;

import com.modong.backend.domain.application.Application;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
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
