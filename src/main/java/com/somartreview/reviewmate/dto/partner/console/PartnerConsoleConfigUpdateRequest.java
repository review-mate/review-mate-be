package com.somartreview.reviewmate.dto.partner.console;

import com.somartreview.reviewmate.domain.partner.console.ConsoleTimeSeriesUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class PartnerConsoleConfigUpdateRequest {

    @NotNull
    @Schema(description = "목표 리뷰 작성률", example = "100")
    private Float targetReviewingRate;

    @NotNull
    @Schema(description = "달성률 기간 단위", example = "QUARTER")
    private ConsoleTimeSeriesUnit achievementTimeSeriesUnit;
}
