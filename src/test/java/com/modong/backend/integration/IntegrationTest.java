package com.modong.backend.integration;

import static com.modong.backend.Fixtures.ClubFixture.CLUB_NAME;
import static com.modong.backend.Fixtures.ClubFixture.CLUB_PROFILE_IMG_URL;
import static com.modong.backend.Fixtures.MemberFixture.EMAIL;
import static com.modong.backend.Fixtures.MemberFixture.MEMBER_ID;
import static com.modong.backend.Fixtures.MemberFixture.NAME;
import static com.modong.backend.Fixtures.MemberFixture.PASSWORD;
import static com.modong.backend.Fixtures.MemberFixture.PHONE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modong.backend.auth.AuthService;
import com.modong.backend.auth.Dto.LoginRequest;
import com.modong.backend.auth.Dto.TokenResponse;
import com.modong.backend.auth.member.Dto.MemberRegisterRequest;
import com.modong.backend.auth.member.MemberService;
import com.modong.backend.domain.club.ClubService;
import com.modong.backend.domain.club.Dto.ClubCreateResponse;
import com.modong.backend.domain.club.Dto.ClubCreateRequest;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Disabled //설정을 상속하기 위한 부모 클래스이므로 실행을 시킬 필요가 없다.
@AutoConfigureMockMvc
@Transactional
public class IntegrationTest {

  @Autowired
  protected MemberService memberService;

  @Autowired
  protected ClubService clubService;

  @Autowired
  protected AuthService authService;

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;


  public ClubCreateResponse clubSetUp(){
    final ClubCreateRequest clubCreateRequest = new ClubCreateRequest(CLUB_NAME,CLUB_PROFILE_IMG_URL);
    return clubService.save(clubCreateRequest);
  }

  public Long memberSetUp(String clubCode){
    final MemberRegisterRequest memberRegisterRequest = MemberRegisterRequest.builder()
        .memberId(MEMBER_ID).email(EMAIL).password(PASSWORD)
        .phone(PHONE).name(NAME).clubCode(clubCode).build();
    return memberService.register(memberRegisterRequest);
  }


  public TokenResponse tokenSetUp(Long memberId){
    final LoginRequest loginRequest = LoginRequest.builder().memberId(MEMBER_ID).password(PASSWORD).build();
    TokenResponse tokenResponse = authService.login(loginRequest);
    return tokenResponse;
  }
}
