package com.modong.backend.domain.club;

import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.domain.club.clubMemeber.ClubMember;
import com.modong.backend.utils.UUIDGenerateUtils;
import java.util.ArrayList;
import javax.persistence.Entity;
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
public class Club extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String profileImgUrl;

  private String clubCode = UUIDGenerateUtils.makeShortUUID(); // 10자리 유니크 아이디 부여

  private String startDate;

  private String endDate;

  private Long countOfMember;
  @OneToMany(mappedBy = "club")
  private List<ClubMember> members = new ArrayList<>();
  public Club(ClubCreateRequest clubCreateRequest) {
    this.name = clubCreateRequest.getName();
    this.profileImgUrl = clubCreateRequest.getProfileImgUrl();
    this.startDate = clubCreateRequest.getStartDate();
    this.endDate = clubCreateRequest.getEndDate();
    this.countOfMember = 0L;
  }
  public Club(Long id, ClubCreateRequest clubCreateRequest) {
    this.id = id;
    this.name = clubCreateRequest.getName();
    this.profileImgUrl = clubCreateRequest.getProfileImgUrl();
    this.startDate = clubCreateRequest.getStartDate();
    this.endDate = clubCreateRequest.getEndDate();
  }
  public void updateOneCountOfMember(Long count) {
    this.countOfMember = count;
  }
}