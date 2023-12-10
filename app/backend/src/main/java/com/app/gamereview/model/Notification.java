package com.app.gamereview.model;

import com.app.gamereview.enums.NotificationParent;
import com.app.gamereview.model.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseModel {
    private String parent;
    private NotificationParent parentType;
    private String message;
    private String user;


}
