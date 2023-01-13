package com.modong.backend.domain.judge;

import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.club.ClubManager.ClubManager;
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
public class Judge {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int score;

  @Lob
  private String comment;

  @Lob
  private String memo;

  @ManyToOne(fetch = FetchType.LAZY)
  private Applicant applicant;

  @ManyToOne(fetch = FetchType.LAZY)
  private ClubManager clubManager;

}
