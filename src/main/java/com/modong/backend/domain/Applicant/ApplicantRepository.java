package com.modong.backend.domain.Applicant;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

  ArrayList<Applicant> findAllByApplicationId(Long applicationId);
}
