package com.modong.backend.auth.member;

import com.modong.backend.Enum.ProviderName;
import com.modong.backend.auth.member.Dto.MemberRegisterRequest;
import com.modong.backend.auth.role.RoleName;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.club.clubMemeber.ClubMember;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String memberId;
  private String password;
  private String name;
  private String email;
  private String phone;
  @Enumerated(EnumType.STRING)
  private ProviderName providerName;

  private Long clubId;
  @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST,CascadeType.MERGE})
  private List<ClubMember> clubs = new ArrayList<>();
  @Enumerated(EnumType.STRING)
  private RoleName role;

  public Member(MemberRegisterRequest memberRegisterRequest) {
    this.memberId = memberRegisterRequest.getMemberId();
    this.name = memberRegisterRequest.getName();
    this.email = memberRegisterRequest.getEmail();
    this.phone = memberRegisterRequest.getPhone();
    this.providerName = ProviderName.MODONG;
    this.role = RoleName.ROLE_USER;
  }

  public void addClub(ClubMember club){
    this.clubs.add(club);
  }

  public void setEncodedPassword(String encodedPassword){
    this.password = encodedPassword;
  }
}
