package com.modong.backend.auth;

import com.modong.backend.auth.Dto.LoginRequest;
import com.modong.backend.auth.Dto.TokenRequest;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.auth.member.Member;
import com.modong.backend.auth.member.MemberRepository;
import com.modong.backend.auth.refreshToken.RefreshToken;
import com.modong.backend.auth.refreshToken.RefreshTokenRepository;
import com.modong.backend.global.exception.auth.PasswordMismatchException;
import com.modong.backend.global.exception.auth.TokenNotFoundException;
import com.modong.backend.global.exception.auth.TokenNotValidException;
import com.modong.backend.global.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final MemberRepository memberRepository; // readOnly
  private final RefreshTokenRepository refreshTokenRepository;//read and write

  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public TokenResponse login(LoginRequest loginRequest) {
    //멤버 존재하는지 확인
    Member findMember = memberRepository.findByMemberId(loginRequest.getMemberId()).orElseThrow(() ->
        new MemberNotFoundException(loginRequest.getMemberId()));
    //비밀번호 검증
    validatePassword(findMember,loginRequest.getPassword());

    // RefreshToken 있는지 확인 후 토큰 두개 반환
    RefreshToken savedToken = refreshTokenRepository.findByMemberId(findMember.getId())
        .orElse(new RefreshToken(issueRefreshToken(findMember),findMember.getId()));
    try {// 토큰 만료기간이 지났으면 재발행
      jwtTokenProvider.validateToken(savedToken.getRefreshToken());
    }catch (TokenNotValidException e){
      savedToken.update(issueRefreshToken(findMember));
    }
    
    refreshTokenRepository.save(savedToken);

    return new TokenResponse(findMember.getId(), issueAccessToken(findMember),savedToken.getRefreshToken());
  }

  @Transactional
  public TokenResponse createAccessToken(TokenRequest tokenRequest) {

    // refreshToken 유효성 검사
    if(!jwtTokenProvider.validateToken(tokenRequest.getRefreshToken())){
      throw new TokenNotValidException();
    }
    // 토큰 정보 추출
    Long memberId;
    try{
       memberId = Long.parseLong(jwtTokenProvider.getPayload(tokenRequest.getRefreshToken()));
    }
    catch (Exception e){ // Long으로 바꾸는데 에러가 발생한다면 Exception을 던짐.
      throw new TokenNotValidException();
    }

    //멤버 존재하는지 확인
    Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
        new MemberNotFoundException(memberId));

    // memberId로 DB에 존재하는 토큰 찾은 후 요청으로 들어온 토큰과 같은지 비교
    RefreshToken findToken = refreshTokenRepository.findByMemberId(memberId)
        .orElseThrow(() ->  new TokenNotFoundException(memberId));

    // 요청으로 받은 토큰과 DB에 존재하는 토큰 같은지 검사
    if(!tokenRequest.getRefreshToken().equals(findToken.getRefreshToken())){
      throw new TokenNotValidException();
    }

    findToken.update(issueRefreshToken(findMember));
    refreshTokenRepository.save(findToken);

    return new TokenResponse(findMember.getId(), issueAccessToken(findMember), findToken.getRefreshToken());

  }

  private String issueAccessToken(final Member findMember) {
    return jwtTokenProvider.createAccessToken(findMember.getId());
  }
  private String issueRefreshToken(final Member findMember) {
    return jwtTokenProvider.createRefreshToken(findMember.getId());
  }


  private void validatePassword(final Member findMember, final String password) {
    if (!passwordEncoder.matches(password, findMember.getPassword())) {
      throw new PasswordMismatchException();
    }
  }

}
