package com.modong.backend.domain.application;

import com.modong.backend.Enum.StatusCode;
import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.member.MemberRepository;
import com.modong.backend.domain.application.Dto.ApplicationCreateRequest;
import com.modong.backend.domain.application.Dto.ApplicationDetailResponse;
import com.modong.backend.domain.application.Dto.ApplicationSimpleResponse;
import com.modong.backend.domain.application.Dto.ApplicationUpdateRequest;
import com.modong.backend.domain.applicationEssential.ApplicationEssential;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.ClubRepository;
import com.modong.backend.domain.essentialQuestion.Dto.EssentialQuestionResponse;
import com.modong.backend.domain.essentialQuestion.EssentialQuestion;
import com.modong.backend.domain.essentialQuestion.EssentialQuestionService;
import com.modong.backend.domain.form.Form;
import com.modong.backend.domain.form.dto.FormResponse;
import com.modong.backend.global.exception.application.ApplicationNotFoundException;
import com.modong.backend.global.exception.application.UrlIdDuplicateException;
import com.modong.backend.global.exception.StatusBadRequestException;
import com.modong.backend.global.exception.auth.NoPermissionCreateException;
import com.modong.backend.global.exception.auth.NoPermissionDeleteException;
import com.modong.backend.global.exception.auth.NoPermissionReadException;
import com.modong.backend.global.exception.auth.NoPermissionUpdateException;
import com.modong.backend.global.exception.club.ClubNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.modong.backend.global.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

  private final ApplicationRepository applicationRepository;
  private final ClubRepository clubRepository;
  private final EssentialQuestionService essentialQuestionService;

  private final MemberRepository memberRepository;

  public ApplicationDetailResponse findDetailById(Long applicationId, Long memberId) {

    Member member = findMemberById(memberId);

    Application application = applicationRepository.findByIdAndIsDeletedIsFalse(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));

    Long clubId = application.getClub().getId();

    if(clubId.equals(member.getClubId())){
      return getDetailResponse(application,clubId);
    }
    else{
      throw new NoPermissionReadException();
    }
  }

  public List<ApplicationSimpleResponse> findAllByClubId(Long clubId, Long memberId) {
    Member member = findMemberById(memberId);

    if(clubId.equals(member.getClubId())){
      List<Application> applications = applicationRepository.findAllByClubIdAndIsDeletedIsFalse(clubId);
      List<ApplicationSimpleResponse> result = new ArrayList<>();
      for(Application application: applications){
        result.add(new ApplicationSimpleResponse(application,clubId));
      }
      return result;
    }
    else{
      throw new NoPermissionReadException();
    }
  }

  @Transactional
  public Long createApplication(ApplicationCreateRequest applicationCreateRequest, Long memberId, Long clubId) {
    Member member = findMemberById(memberId);

    if(clubId.equals(member.getClubId())) {
      Club club = clubRepository.findById(clubId)
              .orElseThrow(() -> new ClubNotFoundException(clubId));

      Application application = new Application(applicationCreateRequest,club);

      //지원서 링크 중복 검사
      if(applicationRepository.existsByUrlIdAndIsDeletedIsFalse(applicationCreateRequest.getUrlId())){
        throw new UrlIdDuplicateException();
      }

      //필수 질문 id로 저장
      setEssentialQuestion(applicationCreateRequest.getEssentialQuestionIds(), application);

      Application saved = applicationRepository.save(application);

      return saved.getId();
    }
    else throw new NoPermissionCreateException();

  }

  private void setEssentialQuestion(List<Long> applicationCreateRequest, Application application) {
    for(Long id : applicationCreateRequest){
      EssentialQuestion essentialQuestion = essentialQuestionService.findById(id);
      ApplicationEssential applicationEssential = new ApplicationEssential(application,essentialQuestion);
      application.addEssential(applicationEssential);
    }
  }


  @Transactional
  //필수 질문 id로 업데이트, 권한 - 사용자의 동아리와 지원서 동아리 일치 및 지원서 모집 전
  public Long updateApplication(Long applicationId, ApplicationUpdateRequest applicationUpdateRequest, Long memberId) {

    Member member = findMemberById(memberId);

    Application application = applicationRepository.findByIdAndIsDeletedIsFalse(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));

    Long clubId = application.getClub().getId();

    if(clubId.equals(member.getClubId()) && application.eqStatus(StatusCode.BEFORE_OPENING)) {
      application.update(applicationUpdateRequest);
      //기존 필수 질문들 삭제
      application.getEssentials().removeAll(application.getEssentials());
      //필수 질문 설정
      setEssentialQuestion(applicationUpdateRequest.getEssentialQuestionIds(), application);

      Application saved = applicationRepository.save(application);

      return saved.getId();
    }
    else throw new NoPermissionUpdateException();
  }



  @Transactional
  public void deleteApplication(Long applicationId, Long memberId) {

    Member member = findMemberById(memberId);

    Application application = applicationRepository.findByIdAndIsDeletedIsFalse(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));

    Long clubId = application.getClub().getId();

    if(clubId.equals(member.getClubId())){
      application.delete();
      applicationRepository.save(application);
    }
    else throw new NoPermissionDeleteException();
  }

  public ApplicationDetailResponse findDetailByUrlId(String urlId) {

    Application application = applicationRepository.findByUrlIdAndIsDeletedIsFalse(urlId).orElseThrow(() -> new ApplicationNotFoundException(urlId));

    return getDetailResponse(application,application.getClub().getId());
  }


  private ApplicationDetailResponse getDetailResponse(Application application, Long clubId) {
    ApplicationDetailResponse response = new ApplicationDetailResponse(application,clubId);

    for(ApplicationEssential applicationEssential : application.getEssentials()){
      response.addEssentialQuestion(new EssentialQuestionResponse(applicationEssential.getEssentialQuestion()));
    }

    for(Form form : application.getForms()){
      response.addForm(new FormResponse(form));
    }

    return response;
  }

  @Transactional
  public Long close(Long applicationId, Long memberId) {

    Member member = findMemberById(memberId);

    Application application = findSimpleById(applicationId);

    Long clubId = application.getClub().getId();

    if(clubId.equals(member.getClubId())){
      if(application.eqStatus(StatusCode.CLOSE)){
        throw new StatusBadRequestException();
      }
      application.close();
      Application saved = applicationRepository.save(application);
      return saved.getId();
    }
    else throw new NoPermissionUpdateException();

  }

  @Transactional
  public Long open(Long applicationId, Long memberId) {

    Member member = findMemberById(memberId);

    Application application = findSimpleById(applicationId);

    Long clubId = application.getClub().getId();

    if(clubId.equals(member.getClubId())){
      if(application.eqStatus(StatusCode.OPEN)){
        throw new StatusBadRequestException();
      }
      application.open();
      Application saved = applicationRepository.save(application);
      return saved.getId();
    }
    else throw new NoPermissionUpdateException();
  }

  public Application findSimpleById(Long applicationId){
    Application application = applicationRepository.findByIdAndIsDeletedIsFalse(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));
    return application;
  }
  private Member findMemberById(Long memberId){
    Member findMember = memberRepository.findByIdAndIsDeletedIsFalse(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    return findMember;
  }
}
