package com.modong.backend.domain.Form;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {

  List<Form> findAllByApplicationId(Long applicationId);

}
