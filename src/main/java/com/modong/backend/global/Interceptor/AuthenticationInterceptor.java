package com.modong.backend.global.Interceptor;

import com.modong.backend.auth.JwtTokenProvider;
import com.modong.backend.auth.support.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
      final Object handler) {
    if (CorsUtils.isCorsRequest(request)) {
      return true;
    }

    String token = AuthorizationExtractor.extract(request);
    jwtTokenProvider.validateToken(token);
    return true;
  }

}
