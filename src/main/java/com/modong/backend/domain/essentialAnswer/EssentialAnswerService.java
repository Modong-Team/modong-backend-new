package com.modong.backend.domain.essentialAnswer;

import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.essentialAnswer.Dto.EssentialAnswerRequest;
import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
import com.modong.backend.domain.essentialQuestion.EssentialQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EssentialAnswerService {

  private final EssentialAnswerRepository essentialAnswerRepository;
  private final EssentialQuestionService essentialQuestionService;
  @Transactional
  public Long create(EssentialAnswerRequest essentialAnswerRequest, Applicant applicant) {
    EssentialQuestion essentialQuestion = essentialQuestionService.findById(essentialAnswerRequest.getEssentialQuestionId());

    EssentialAnswer essentialAnswer = new EssentialAnswer(essentialAnswerRequest,essentialQuestion,applicant);

    essentialAnswerRepository.save(essentialAnswer);

    return essentialAnswer.getId();
  }
}
