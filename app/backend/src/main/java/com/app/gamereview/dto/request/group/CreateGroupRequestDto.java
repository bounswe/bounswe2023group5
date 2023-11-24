package com.app.gamereview.dto.request.group;

import com.app.gamereview.enums.MembershipPolicy;
import com.app.gamereview.util.validation.annotation.ValidMemberPolicy;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateGroupRequestDto {

    @NotEmpty(message = "Title field cannot be null or empty")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    @Size(max = 25, message = "Title must be at most 25 characters long")
    private String title;

    @NotNull(message = "Description field cannot be null")
    @Size(max = 600, message = "Description must be at most 600 characters long")
    private String description;

    @ValidMemberPolicy(allowedValues = {MembershipPolicy.PUBLIC, MembershipPolicy.PRIVATE})
    private String membershipPolicy;

    private List<String> tags = new ArrayList<>(); // list of tag ids

    @NotEmpty(message = "Game Id must be provided")
    @Pattern(regexp = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$",
            message = "Game has Invalid Id (UUID) format")
    private String gameId;      // id of related game

    @Positive(message = "Quota cannot be negative or zero")
    private int quota;

    @NotNull(message = "Avatar only field cannot be null")
    private Boolean avatarOnly = false;
}
