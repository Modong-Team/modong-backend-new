package com.modong.backend.domain.applicant.repository;

import com.modong.backend.domain.applicant.Applicant;
import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

  ArrayList<Applicant> findAllByApplicationId(Long applicationId);
}
