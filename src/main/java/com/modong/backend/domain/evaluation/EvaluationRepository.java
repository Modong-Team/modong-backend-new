package com.modong.backend.domain.evaluation;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {

  Optional<Evaluation> findByIdAndIsDeletedIsFalse(Long evaluationId);

  Optional<Evaluation> findByApplicantIdAndMemberIdAndIsDeletedIsFalse(Long applicantId,Long memberId);
  List<Evaluation> findAllByApplicantIdAndIsDeletedIsFalse(Long evaluationId);

  Boolean existsByApplicantIdAndMemberIdAndIsDeletedIsFalse(Long applicantId, Long memberId);


}
