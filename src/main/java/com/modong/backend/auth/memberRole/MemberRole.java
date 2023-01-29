package com.modong.backend.auth.memberRole;

import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.role.Role;
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
public class MemberRole {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;
  @ManyToOne(fetch = FetchType.LAZY)
  private Role role;

  public MemberRole(Member member, Role role) {
    this.member = member;
    this.role = role;
  }
}

