package com.modong.backend.domain.club;

import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.member.MemberRepository;
import com.modong.backend.domain.club.Dto.ClubCheckRequest;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.domain.club.Dto.ClubResponse;
import com.modong.backend.domain.club.Dto.ClubCreateResponse;
import com.modong.backend.domain.club.clubMemeber.ClubMember;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

  private final ClubRepository clubRepository;
  private final MemberRepository memberRepository;
  public ClubResponse findById(Long clubId) {
    Club club = clubRepository.findById(clubId).orElseThrow(() -> new ClubNotFoundException(clubId));
    return new ClubResponse(club);
  }
  public ClubResponse findByClubCode(String clubCode) {
    Club club = clubRepository.findByClubCode(clubCode).orElseThrow(() -> new ClubNotFoundException(clubCode));
    return new ClubResponse(club);
  }

  public boolean checkClubCode(ClubCheckRequest clubCheckRequest){
    return isExistClubCode(clubCheckRequest.getClubCode());
  }

  private boolean isExistClubCode(String clubCode) {
    return clubRepository.existsByClubCode(clubCode);
  }

  @Transactional
  public ClubCreateResponse save(ClubCreateRequest clubCreateRequest) {
    Club savedClub = clubRepository.save(new Club(clubCreateRequest));
    return new ClubCreateResponse(savedClub);
  }

  public ClubResponse findByMemberId(Long memberId) {
    //회원 조회 실패시 에러 반환
    Member member = memberRepository.findByIdAndIsDeletedIsFalse(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    Long clubId = member.getClubId();
    Club club = clubRepository.findById(clubId).orElseThrow(() -> new ClubNotFoundException(clubId));
    return new ClubResponse(club);
  }
}
