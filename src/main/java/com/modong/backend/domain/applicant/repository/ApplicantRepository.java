package com.modong.backend.domain.applicant.repository;

import com.modong.backend.Enum.ApplicantStatus;
import com.modong.backend.domain.applicant.Applicant;
import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

  ArrayList<Applicant> findAllByApplicationId(Long applicationId);
  ArrayList<Applicant> findAllByApplicantStatus(Long applicationId);

  Page<Applicant> findAllByApplicationIdAndApplicantStatusAndDeletedIsFalse(Long applicationId, ApplicantStatus applicantStatus,
      Pageable pageable);

}
