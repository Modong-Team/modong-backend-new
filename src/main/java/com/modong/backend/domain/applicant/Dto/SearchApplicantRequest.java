package com.modong.backend.domain.applicant.Dto;

import com.modong.backend.base.Dto.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@Schema(name = "지원자들 조회 요청 ACCEPT(2),APPLICATION(3),INTERVIEW(4),SUCCESS(5)")
public class SearchApplicantRequest{

  @NotNull
  @Range(max = 5,min = 2, message = "지원사 상태값은 2에서 5사이의 숫자여야 합니다!")
  @Schema(description = "지원자 상태", example = "2")
  private int applicantStatusCode = 2;

  @Schema(description = "지원자 필터", example = "all/evaluating/fail")
  private String filter = "all";
}
