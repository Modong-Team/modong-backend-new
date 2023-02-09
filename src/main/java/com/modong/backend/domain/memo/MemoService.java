package com.modong.backend.domain.memo;

import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.member.MemberRepository;
import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.applicant.repository.ApplicantRepository;
import com.modong.backend.domain.application.Application;
import com.modong.backend.domain.application.ApplicationRepository;
import com.modong.backend.domain.club.clubMemeber.ClubMember;
import com.modong.backend.domain.memo.dto.MemoCreateRequest;
import com.modong.backend.domain.memo.dto.MemoDeleteRequest;
import com.modong.backend.domain.memo.dto.MemoFindRequest;
import com.modong.backend.domain.memo.dto.MemoResponse;
import com.modong.backend.domain.memo.dto.MemoUpdateRequest;
import com.modong.backend.global.exception.ResourceNotFoundException;
import com.modong.backend.global.exception.applicant.ApplicantNotFoundException;
import com.modong.backend.global.exception.application.ApplicationNotFoundException;
import com.modong.backend.global.exception.auth.NoPermissionCreateException;
import com.modong.backend.global.exception.auth.NoPermissionDeleteException;
import com.modong.backend.global.exception.auth.NoPermissionReadException;
import com.modong.backend.global.exception.auth.NoPermissionUpdateException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {

  private final MemberRepository memberRepository;
  private final MemoRepository memoRepository;
  private final ApplicantRepository applicantRepository;
  private final ApplicationRepository applicationRepository;

  @Transactional
  public Long create(MemoCreateRequest memoCreateRequest, Long memberId) {
    Long applicationId = memoCreateRequest.getApplicationId();
    Long applicantId = memoCreateRequest.getApplicantId();

    //회원 조회 실패시 에러 반환
    Member member = memberRepository.findByIdAndIsDeletedIsFalse(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    //지원자 조회 실패시 에러 반환
    Applicant applicant = applicantRepository.findByIdAndIsDeletedIsFalse(applicantId).orElseThrow(() -> new ApplicantNotFoundException(applicantId));
    //지원서 조회 실패시 에러 반환
    Application application = applicationRepository.findByIdAndIsDeletedIsFalse(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));

    //회원이 지원자에 대한 메모를 남길 권한이 있는지를 동아리가 같은지 비교
    for(ClubMember clubMember : member.getClubs()){
      if(application.getClub().getId().equals(clubMember.getClub().getId())){
        Memo memo = new Memo(memoCreateRequest,member,applicant);
        Memo saved = memoRepository.save(memo);
        return saved.getId();
      }
    }
    throw new NoPermissionCreateException();
  }

  //작성자만 수정 가능
  @Transactional
  public Long update(MemoUpdateRequest memoUpdateRequest, Long memberId) {
    Long memoId = memoUpdateRequest.getMemoId();
    //회원 조회 실패시 에러 반환
    Member member = memberRepository.findByIdAndIsDeletedIsFalse(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

    //메모 조회 실패시 에러 반환
    Memo memo = memoRepository.findByIdAndIsDeletedIsFalse(memoId).orElseThrow(() -> new ResourceNotFoundException("Memo",memoId));

    //작성자가 회원이 맞는지 검증
    if(memo.getCreatorId().equals(member.getId())){
      memo.update(memoUpdateRequest);
      Memo saved = memoRepository.save(memo);
      return saved.getId();
    }
    else throw new NoPermissionUpdateException();
  }


  //작성자만 삭제 가능
  @Transactional
  public void delete(MemoDeleteRequest memoDeleteRequest, Long memberId) {
    Long memoId = memoDeleteRequest.getMemoId();
    //회원 조회 실패시 에러 반환
    Member member = memberRepository.findByIdAndIsDeletedIsFalse(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    //메모 조회 실패시 에러 반환
    Memo memo = memoRepository.findByIdAndIsDeletedIsFalse(memoId).orElseThrow(() -> new ResourceNotFoundException("Memo",memoId));
    //작성자가 회원이 맞는지 검증
    if(memo.getCreatorId().equals(member.getId())){
      memo.delete();
      memoRepository.save(memo);
    }
    else throw new NoPermissionDeleteException();
  }

  public List<MemoResponse> findAllByApplication(MemoFindRequest memoFindRequest, Long memberId) {
    Long applicationId = memoFindRequest.getApplicationId();
    Long applicantId = memoFindRequest.getApplicantId();
    //회원 조회 실패시 에러 반환
    Member member = memberRepository.findByIdAndIsDeletedIsFalse(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    //지원자 조회 실패시 에러 반환
    Applicant applicant = applicantRepository.findByIdAndIsDeletedIsFalse(applicantId).orElseThrow(() -> new ApplicantNotFoundException(applicantId));
    //지원서 조회 실패시 에러 반환
    Application application = applicationRepository.findByIdAndIsDeletedIsFalse(applicationId).orElseThrow(() -> new ApplicationNotFoundException(applicationId));

    boolean havePermission = false;
    List<Memo> memos = new ArrayList<>();

    //회원이 지원자에 대한 메모를 조회할 권한이 있는지를 동아리가 같은지 비교
    for(ClubMember clubMember : member.getClubs()){
      if(applicant.getApplication().getClub().equals(clubMember.getClub())){
        havePermission = true;
        break;
      }
    }

    if(havePermission){
      List<MemoResponse> result = new ArrayList<>();
      memos = memoRepository.findAllByApplicantIdAndIsDeletedIsFalse(applicant.getId());
      for(Memo memo : memos){
        Member writer = memo.getMember();
        result.add(new MemoResponse(memo,writer,application.getId(),applicant.getId()));
      }
      return result;
    }
    else{
      throw new NoPermissionReadException();
    }
  }
}
