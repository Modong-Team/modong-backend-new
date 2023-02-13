package com.modong.backend.domain.evaluation;

import com.modong.backend.base.BaseEntity;
import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.auth.member.Member;
import com.modong.backend.domain.evaluation.dto.EvaluationCreateRequest;
import com.modong.backend.domain.evaluation.dto.EvaluationUpdateRequest;
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
public class Evaluation extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Float score;
  @Lob
  private String comment;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  private Applicant applicant;

  public Evaluation(EvaluationCreateRequest evaluationCreateRequest, Member member, Applicant applicant) {
    this.creatorId = member.getId();
    this.score = evaluationCreateRequest.getScore();
    this.comment = evaluationCreateRequest.getComment();
    this.member = member;
    this.applicant = applicant;
  }

  public boolean isWriter(Member member){
    return this.creatorId.equals(member.getId());
  }
  public void update(EvaluationUpdateRequest evaluationUpdateRequest){
    this.score = evaluationUpdateRequest.getNewScore();
    this.comment = evaluationUpdateRequest.getNewComment();
  }

  public void delete() {
    this.isDeleted = true;
  }
}
