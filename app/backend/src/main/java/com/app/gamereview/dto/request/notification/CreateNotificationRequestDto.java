package com.app.gamereview.dto.request.notification;

import com.app.gamereview.enums.NotificationParent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationRequestDto {
    private String parent;
    private NotificationParent parentType;
    private String message;
    private String user;
}
