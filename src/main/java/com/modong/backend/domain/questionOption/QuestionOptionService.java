package com.modong.backend.domain.questionOption;

import com.modong.backend.domain.questionOption.Dto.QuestionOptionResponse;
import com.modong.backend.domain.question.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionOptionService {

  private final QuestionOptionRepository questionOptionRepository;

  @Transactional
  public QuestionOption create(String content, Question question) {
    QuestionOption questionOption = new QuestionOption(content,question);
    questionOptionRepository.save(questionOption);
    return questionOption;
  }
  @Transactional
  public List<QuestionOption> createList(List<String> questionOptionRequest, Question question){
    List<QuestionOption> results = new ArrayList<>();
    for(String value : questionOptionRequest){
      results.add(create(value,question));
    }
    return results;
  }
  public List<QuestionOptionResponse> findAllByQuestion(Question question) {

    List<QuestionOptionResponse> options = questionOptionRepository.findAllByQuestion(question).stream().map(
        QuestionOptionResponse::new).collect(
        Collectors.toList());
    return options;

  }
}
