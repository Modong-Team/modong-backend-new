package com.modong.backend.domain.club;

import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.domain.club.Dto.ClubResponse;
import com.modong.backend.domain.club.Dto.ClubCreateResponse;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

  private final ClubRepository clubRepository;
  public ClubResponse findById(Long clubId) {
    Club club = clubRepository.findById(clubId).orElseThrow(() -> new ClubNotFoundException(clubId));
    return new ClubResponse(club);
  }
  public ClubResponse findByClubCode(String clubCode) {
    Club club = clubRepository.findByClubCode(clubCode).orElseThrow(() -> new ClubNotFoundException(clubCode));
    return new ClubResponse(club);
  }

  public void checkClubCode(ClubCheckRequest clubCheckRequest){
    if(isExistClubCode(clubCheckRequest.getClubCode())){
      throw new ClubNotFoundException(clubCheckRequest.getClubCode());
    }
  }

  private boolean isExistClubCode(String clubCode) {
    return clubRepository.existsByClubCode(clubCode);
  }

  @Transactional
  public ClubCreateResponse save(ClubCreateRequest clubCreateRequest) {
    Club savedClub = clubRepository.save(new Club(clubCreateRequest));
    return new ClubCreateResponse(savedClub);
  }
}
