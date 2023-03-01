package com.modong.backend.domain.form;

import static com.modong.backend.Enum.CustomCode.ERROR_REQ_PARAM_ID;

import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.member.MemberRepository;
import com.modong.backend.domain.application.Application;
import com.modong.backend.domain.application.ApplicationService;
import com.modong.backend.domain.form.dto.FormRequest;
import com.modong.backend.domain.form.dto.FormResponse;
import com.modong.backend.domain.question.Dto.QuestionRequest;
import com.modong.backend.domain.question.Question;
import com.modong.backend.domain.question.QuestionService;
import java.util.ArrayList;
import java.util.List;

import com.modong.backend.global.exception.auth.NoPermissionCreateException;
import com.modong.backend.global.exception.auth.NoPermissionDeleteException;
import com.modong.backend.global.exception.auth.NoPermissionReadException;
import com.modong.backend.global.exception.auth.NoPermissionUpdateException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormService {

  private final FormRepository formRepository;
  private final QuestionService questionService;
  private final ApplicationService applicationService;
  private final MemberRepository memberRepository;

  @Transactional
  public Long create(FormRequest formRequest, Long memberId) {
    Member member = findMemberById(memberId);

    Application application = applicationService.findSimpleById(formRequest.getApplicationId());

    Long clubId = application.getClub().getId();

    if(clubId.equals(member.getClubId())){
      Form form = new Form(formRequest,application);

      form.getQuestions().clear();

      for(QuestionRequest questionRequest : formRequest.getQuestionRequests()){
        Question question = questionService.create(questionRequest,form);
        form.addQuestion(question);
      }

      formRepository.save(form);

      return form.getId();
    }
    else throw new NoPermissionCreateException();
  }

  public FormResponse findById(Long formId) {

    Form form = formRepository.findByIdAndIsDeletedIsFalse(formId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));

    return new FormResponse(form);
  }

  public List<FormResponse> findAllByApplicationId(Long applicationId) {

    List<Form> forms = formRepository.findAllByApplicationIdAndIsDeletedIsFalse(applicationId);

    List<FormResponse> results = new ArrayList<>();

    for(Form form : forms){
      results.add(new FormResponse(form));
    }

    return results;
  }

  @Transactional
  public Long update(Long formId, FormRequest formRequest, Long memberId) {

    Member member = findMemberById(memberId);

    Form form = formRepository.findByIdAndIsDeletedIsFalse(formId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));

    Long clubId = form.getApplication().getClub().getId();

    if(clubId.equals(member.getClubId())){
      form.updateForm(formRequest);
      //기존 질문들 삭제
      form.getQuestions().removeAll(form.getQuestions());

      for(QuestionRequest questionRequest : formRequest.getQuestionRequests()){
        Question question = questionService.create(questionRequest,form);
        form.addQuestion(question);
      }

      formRepository.save(form);

      return form.getId();
    }
    else throw new NoPermissionUpdateException();

  }

  @Transactional
  public void deleteForm(Long formId, Long memberId) {

    Member member = findMemberById(memberId);

    Form form = formRepository.findByIdAndIsDeletedIsFalse(formId).orElseThrow(() -> new IllegalArgumentException(ERROR_REQ_PARAM_ID.toString()));

    Long clubId = form.getApplication().getClub().getId();
    if (clubId.equals(member.getClubId())) {
      form.delete();
      formRepository.save(form);
    }
    else throw new NoPermissionDeleteException();
  }
  private Member findMemberById(Long memberId){
    Member findMember = memberRepository.findByIdAndIsDeletedIsFalse(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    return findMember;
  }

}
