package com.modong.backend.domain.applicant.repository;

import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.applicant.Dto.SearchApplicantRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicantRepositoryCustom {

  void updateRateByApplicantId(Long applicantId);

  Float getRateByApplicantId(Long applicantId);
  Page<Applicant> searchByApplicationIdAndStatus(Long applicationId, SearchApplicantRequest request, Pageable pageable);
}
