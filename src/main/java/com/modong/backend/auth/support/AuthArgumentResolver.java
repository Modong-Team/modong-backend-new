package com.modong.backend.auth.support;

import com.modong.backend.auth.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public boolean supportsParameter(final MethodParameter parameter) {
    return parameter.hasParameterAnnotation(Auth.class);
  }

  @Override
  public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
      final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
    String token = AuthorizationExtractor.extract(toHttpServletRequest(webRequest));
    jwtTokenProvider.validateToken(token);
    return Long.valueOf(jwtTokenProvider.getPayload(token));
  }

  private HttpServletRequest toHttpServletRequest(final NativeWebRequest webRequest) {
    return webRequest.getNativeRequest(HttpServletRequest.class);
  }
}
