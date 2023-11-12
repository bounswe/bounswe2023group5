package com.app.gamereview.dto.request.user;

import com.app.gamereview.enums.UserRole;

import com.app.gamereview.util.validation.annotation.ValidUserRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeRoleRequestDto {

    @ValidUserRole(allowedValues = {UserRole.BASIC, UserRole.ADMIN })
    private String userRole;
}
