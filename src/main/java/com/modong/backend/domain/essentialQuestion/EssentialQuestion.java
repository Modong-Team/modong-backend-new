package com.modong.backend.domain.essentialQuestion;

import com.modong.backend.domain.applicationEssential.ApplicationEssential;
import com.modong.backend.base.BaseTimeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EssentialQuestion extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  private boolean isRequire;

  @OneToMany(mappedBy = "essentialQuestion", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<ApplicationEssential> applications = new ArrayList<>();

  @Builder
  public EssentialQuestion(String content, boolean isRequire) {
    this.content = content;
    this.isRequire = isRequire;
  }
}