package com.modong.backend.domain.application;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

  Optional<Application> findById(Long id);
  List<Application> findAllByClubId(Long clubId);

  Optional<Application> findByUrlId(String urlId);
  boolean existsByUrlId(String urlId);
}