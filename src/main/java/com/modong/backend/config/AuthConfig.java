package com.modong.backend.config;

import com.modong.backend.auth.JwtTokenProvider;
import com.modong.backend.auth.support.AuthArgumentResolver;
import com.modong.backend.global.Interceptor.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new AuthArgumentResolver(jwtTokenProvider));
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(new AuthenticationInterceptor(jwtTokenProvider))
        .addPathPatterns("/api/v1/**")
        .excludePathPatterns(
                "/api/v1/club", "api/v1/club/check",
                "/api/v1/login","/api/v1/register","/api/v1/member/check", "/api/v1/token",
                "/api/v1/view/application/**", "/api/v1/form/**", "api/v1/forms/**",
                "/api/v1/applicant");
  }

}
