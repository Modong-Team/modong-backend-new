package com.modong.backend.config;

// Querydsl 의 쿼리를 작성할 JPAQueryFactory 클래스를 빈으로 등록

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuerydslConfig {
  private final EntityManager em;

  @Bean
  public JPAQueryFactory queryFactory() {
    return new JPAQueryFactory(em);
  }
}

