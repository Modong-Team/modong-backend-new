package com.modong.backend.domain.club.clubMemeber;

import com.modong.backend.auth.member.Member;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.club.Club;
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
public class ClubMember extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  private Club club;

  public ClubMember(Club club, Member member) {
    this.member = member;
    this.club = club;
  }
}
