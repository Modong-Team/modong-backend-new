package com.modong.backend.domain.memo;

import com.modong.backend.auth.member.Member;
import com.modong.backend.base.BaseEntity;
import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.memo.dto.MemoCreateRequest;
import com.modong.backend.domain.memo.dto.MemoUpdateRequest;
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
public class Memo extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Lob
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  private Applicant applicant;

  public Memo(MemoCreateRequest memoCreateRequest, Member member, Applicant applicant) {
    this.creatorId = member.getId();
    this.content = memoCreateRequest.getContent();
    this.member = member;
    this.applicant = applicant;
  }

  public void update(MemoUpdateRequest memoUpdateRequest){
    this.content = memoUpdateRequest.getContent();
  }
}
