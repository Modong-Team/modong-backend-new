package com.modong.backend.domain.applicant.repository;

import static com.modong.backend.domain.applicant.QApplicant.applicant;

import com.modong.backend.Enum.ApplicantStatus;
import com.modong.backend.domain.applicant.Applicant;
import com.modong.backend.domain.applicant.Dto.SearchApplicantRequest;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplicantRepositoryCustomImpl implements ApplicantRepositoryCustom{
  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Applicant> searchByApplicationIdAndStatus(Long applicationId, SearchApplicantRequest request,
      Pageable pageable) {

    final int status = request.getApplicantStatusCode();
    final String filter = request.getFilter();
    //content를 가져오는 쿼리
    List<Applicant> fetch = queryFactory
        .selectFrom(applicant)
        .where(
            filtering(filter),
            eqId(applicationId),
            eqStatus(status),
            eqNotDeleted()
        )
        .orderBy(makeSort(pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    //count 만 가져오는 쿼리
    JPQLQuery<Applicant> count = queryFactory
        .selectFrom(applicant)
        .where(
            filtering(filter),
            eqId(applicationId),
            eqStatus(status),
            eqNotDeleted()
        );
    return PageableExecutionUtils.getPage(fetch,pageable,()-> count.fetchCount());
  }

  private BooleanExpression filtering(String filter) {
    switch (filter){
      case "fail":{
        return applicant.isFail.eq(true);
      }
      case "evaluating":{
        return applicant.isFail.eq(false);
      }
      default:{
        return null;
      }
    }
  }

  private BooleanExpression eqNotFail(boolean isFail) {
    return applicant.isFail.eq(isFail);
  }

  private BooleanExpression eqId(Long id) {
    return applicant.application.id.eq(id);
  }

  private BooleanExpression eqStatus(int status) {
    if(status == 0) {return null;}
    return applicant.applicantStatus.eq(ApplicantStatus.valueOf(status));
  }
  private BooleanExpression eqNotDeleted() {
    return applicant.isDeleted.isFalse();
  }

  private OrderSpecifier[] makeSort(Sort sort) {
    List<OrderSpecifier> orders = new ArrayList<>();

    for(Sort.Order order : sort) {
      Order direction = order.isAscending() ? Order.ASC : Order.DESC;
      String property = order.getProperty();

      PathBuilder conditions = new PathBuilder(Applicant.class, "applicant");
      orders.add(new OrderSpecifier(Order.ASC, applicant.isFail));
      orders.add(new OrderSpecifier(direction, conditions.get(property)));
    }

    return orders.stream().toArray(OrderSpecifier[]::new);
  }

}
