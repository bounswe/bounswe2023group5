package com.app.gamereview.dto.request.group;

import com.app.gamereview.enums.GroupApplicationReviewResult;
import com.app.gamereview.util.validation.annotation.ValidApplicationReviewResult;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GroupApplicationReviewDto {

    @NotEmpty(message = "Application message cannot be null or empty")
    @ValidApplicationReviewResult(allowedValues = {GroupApplicationReviewResult.APPROVE, GroupApplicationReviewResult.REJECT})
    private String result;
}
