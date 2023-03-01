package com.modong.backend.domain.applicant.repository;

import com.modong.backend.domain.applicant.Applicant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

  ArrayList<Applicant> findAllByApplicationIdAndIsDeletedIsFalse(Long applicationId);

  Optional<Applicant> findByIdAndIsDeletedIsFalse(Long applicantId);

  Long countAllByApplicationIdAndRealNameAndIsDeletedIsFalse(Long applicationId,String name);
}
