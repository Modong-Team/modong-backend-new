package com.modong.backend.domain.question;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {

  List<Question> findAllByFormId(Long formId);
}
