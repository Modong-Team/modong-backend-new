package com.modong.backend.domain.questionOption;

import com.modong.backend.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface QuestionOptionRepository extends JpaRepository<QuestionOption,Long> {

  List<QuestionOption> findAllByQuestion(Question question);

}
