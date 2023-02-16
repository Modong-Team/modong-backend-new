package com.modong.backend.unit.domain.club;

import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.ClubRepository;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import com.modong.backend.unit.base.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.modong.backend.Fixtures.ClubFixture.CLUB_END_DATE;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_NAME;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_PROFILE_IMG_URL;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_START_DATE;
import static org.junit.jupiter.api.Assertions.*;

public class ClubRepositoryTest extends RepositoryTest {

  @Autowired
  private ClubRepository clubRepository;

  @Test
  @DisplayName(value = "동아리 생성")
  public void createClub(){
    //given
    Club club = new Club(new ClubCreateRequest(CLUB_NAME,CLUB_PROFILE_IMG_URL,CLUB_START_DATE,CLUB_END_DATE));
    //then
    Club savedClub =  clubRepository.save(club);
    //result
    assertEquals(club,savedClub);
  }
}
