package com.modong.backend.domain.form;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FormRepository extends JpaRepository<Form, Long> {

  Optional<Form> findByIdAndIsDeletedIsFalse(Long formId);
  List<Form> findAllByApplicationIdAndIsDeletedIsFalse(Long applicationId);

}
