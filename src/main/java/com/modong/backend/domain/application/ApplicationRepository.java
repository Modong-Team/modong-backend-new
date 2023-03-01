package com.modong.backend.domain.application;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

  Optional<Application> findById(Long id);
  List<Application> findAllByClubIdAndIsDeletedIsFalse(Long clubId);

  Optional<Application> findByUrlIdAndIsDeletedIsFalse(String urlId);
  boolean existsByUrlIdAndIsDeletedIsFalse(String urlId);

  Optional<Application> findByIdAndIsDeletedIsFalse(Long id);

}