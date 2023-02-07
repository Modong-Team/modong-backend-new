package com.modong.backend.domain.applicant.repository;

import com.modong.backend.Enum.ApplicantStatus;
import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.applicant.Dto.SearchApplicantRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicantRepositoryCustom {
  Page<Applicant> searchByApplicationIdAndStatus(Long applicationId, SearchApplicantRequest request, Pageable pageable);
}
