package com.modong.backend.auth.member;

import com.modong.backend.auth.member.Dto.MemberRegisterRequest;
import com.modong.backend.auth.member.Dto.MemberCheckRequest;
import com.modong.backend.auth.member.Dto.MemberResponse;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.club.ClubRepository;
import com.modong.backend.domain.club.clubMemeber.ClubMember;
import com.modong.backend.global.exception.club.ClubNotFoundException;
import com.modong.backend.global.exception.member.DuplicateEmailException;
import com.modong.backend.global.exception.member.DuplicateMemberIdException;
import com.modong.backend.global.exception.member.DuplicatePhoneException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  private final ClubRepository clubRepository;

  private final PasswordEncoder passwordEncoder;
  @Transactional
  public Long register(MemberRegisterRequest memberRegisterRequest) {

    Club club = clubRepository.findByClubCode(memberRegisterRequest.getClubCode())
        .orElseThrow(() -> new ClubNotFoundException(memberRegisterRequest.getClubCode()));

//    //휴대폰 번호 중복 검사
//    if(isDuplicatePhone(memberRegisterRequest.getPhone())){
//      throw new DuplicatePhoneException();
//    }
//    //이메일 중복 검사
//    if(isDuplicateEmail(memberRegisterRequest.getEmail())){
//      throw new DuplicateEmailException();
//    }
    // 회원 아이디 중복 검사
    if(isDuplicateMemberId(memberRegisterRequest.getMemberId())){
      throw new DuplicateMemberIdException();
    }

    Member member = new Member(memberRegisterRequest,club.getId());

    member.setEncodedPassword(passwordEncoder.encode(memberRegisterRequest.getPassword()));

    Member savedMember = memberRepository.save(member);

    club.updateOneCountOfMember(memberRepository.countByClubIdAndIsDeletedIsFalse(club.getId()));

    clubRepository.save(club);

    return savedMember.getId();
  }

  public MemberResponse findById(Long findMemberId) {
    Member member = memberRepository.findById(findMemberId).orElseThrow(() -> new MemberNotFoundException(findMemberId));
    return new MemberResponse(member);
  }
  public boolean checkMemberId(MemberCheckRequest memberCheckRequest){
    return isDuplicateMemberId(memberCheckRequest.getMemberId());
  }


  private boolean isDuplicateEmail(String email) {
    return memberRepository.existsByEmail(email);
  }

  private boolean isDuplicatePhone(String phone) {
    return memberRepository.existsByPhone(phone);
  }

  private boolean isDuplicateMemberId(String memberId) {
    return memberRepository.existsByMemberId(memberId);
  }

}
