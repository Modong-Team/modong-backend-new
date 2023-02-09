package com.modong.backend.domain.memo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MemoRepository extends JpaRepository<Memo,Long> {

  Optional<Memo> findByIdAndIsDeletedIsFalse(Long memoId);
  List<Memo> findAllByApplicantIdAndIsDeletedIsFalse(Long applicationId);
}
