package com.modong.backend.unit.domain.club;

import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.ClubRepository;
import com.modong.backend.unit.base.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

public class ClubRepositoryTest extends RepositoryTest {

  @Autowired
  private ClubRepository clubRepository;

  @Test
  @DisplayName(value = "클럽 생성")
  public void createClub(){
    //given
    Club club = new Club("모동","테스트용 모동 동아리 입니다!","fbHl9VTmh7");
    //then
    Club savedClub =  clubRepository.save(club);
    //result
    assertEquals(club,savedClub);
  }
}