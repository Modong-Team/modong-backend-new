package com.modong.backend.Application;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

  List<Application> findAllByClubId(Long clubId);

  Optional<Application> findByUrlId(String urlId);
}