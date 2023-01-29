package com.modong.backend.unit.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modong.backend.auth.AuthService;
import com.modong.backend.auth.JwtTokenProvider;
import com.modong.backend.auth.member.MemberService;
import com.modong.backend.config.AuthConfig;
import com.modong.backend.domain.club.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@MockBean({JwtTokenProvider.class,AuthConfig.class})
public class ControllerTest {

  @MockBean
  protected MemberService memberService;

  @MockBean
  protected ClubService clubService;

  @MockBean
  protected AuthService authService;

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;



}
