package com.app.gamereview.dto.request.group;

import com.app.gamereview.enums.MembershipPolicy;
import com.app.gamereview.util.validation.annotation.ValidMemberPolicy;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGroupRequestDto {

    @NotEmpty(message = "Title field cannot be null or empty")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    @Size(max = 25, message = "Title must be at most 25 characters long")
    private String title;

    @NotNull(message = "Description field cannot be null")
    @Size(max = 600, message = "Description must be at most 600 characters long")
    private String description;

    @ValidMemberPolicy(allowedValues = {MembershipPolicy.PUBLIC, MembershipPolicy.PRIVATE})
    private String membershipPolicy;

    private String groupIcon;

    @Positive(message = "Quota cannot be negative or zero")
    private int quota;

    @NotNull(message = "Avatar only field cannot be null")
    private Boolean avatarOnly;
}
