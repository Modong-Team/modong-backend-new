package com.modong.backend.domain.question;

import static com.modong.backend.Enum.CustomCode.ERROR_REQ_PARAM_ID;

import com.modong.backend.Enum.QuestionType;
import com.modong.backend.domain.form.Form;
import com.modong.backend.domain.questionOption.QuestionOption;
import com.modong.backend.domain.questionOption.QuestionOptionService;
import com.modong.backend.domain.question.Dto.QuestionRequest;
import com.modong.backend.domain.question.Dto.QuestionResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

  private final QuestionRepository questionRepository;

  private final QuestionOptionService questionOptionService;

  public Question findById(Long questionId) {
    Question question = questionRepository.findById(questionId)
        .orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));
    return question;
  }

  @Transactional
  // 일단 중복검사 없이 로직 작성 추후에 추가할 수도 있을듯
  public Question create(QuestionRequest questionRequest, Form form) {

    Question question = new Question(questionRequest,form);
    question.getQuestionOptions().clear();

//    for(QuestionOptionRequest questionOptionRequest : questionRequest.getQuestionOptionRequests()){
//      questionOption questionOption = new questionOption(questionOptionRequest,question);
//      question.addOption(questionOption);
//    }
    if(question.getQuestionType() == QuestionType.SINGLE_SELECT_QUESTION ||
          question.getQuestionType() == QuestionType.MULTI_SELECT_QUESTION) {
        List<QuestionOption> questionOptions = questionOptionService.createList(
            questionRequest.getQuestionOptionRequest(), question);
        question.setOption(questionOptions);

    }
    questionRepository.save(question);

    return question;
  }

  public List<QuestionResponse> findAllByForm(Form form) {

    List<Question> questions = questionRepository.findAllByFormId(form.getId());

    List<QuestionResponse> results = new ArrayList<>();

    for(Question question : questions){
//      List<QuestionOptionResponse> options = new ArrayList<>();
//      if(question.getQuestionType() == QuestionType.SINGLE_SELECT_QUESTION ||
//          question.getQuestionType() == QuestionType.MULTI_SELECT_QUESTION){
//        options = questionOptionService.findAllByQuestion(question);
//      }
      results.add(new QuestionResponse(question));
    }

    return results;
  }

}
