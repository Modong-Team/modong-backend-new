package com.modong.backend.domain.club;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club,Long> {

  Optional<Club> findByClubCode(String clubCode);

  boolean existsByClubCode(String clubCode);
}
