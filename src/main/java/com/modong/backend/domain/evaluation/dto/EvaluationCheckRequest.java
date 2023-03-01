package com.modong.backend.domain.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Schema(name = "평가 했었는지 검사 요청")
public class EvaluationCheckRequest {
    @Schema(description = "지원자 ID",  example = "2")
    @NotNull(message = "지원자 id는 필수 항목입니다!")
    private Long applicantId;

    @Builder
    public EvaluationCheckRequest(Long applicantId) {
        this.applicantId = applicantId;
    }
}