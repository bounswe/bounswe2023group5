package com.app.gamereview.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.app.gamereview.enums.ForumType;
import com.app.gamereview.model.common.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "Forum")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Forum extends BaseModel {

  private String name;

  private ForumType type;

  private String parent; // Group or game that the forum is connected to.

  private List<String> subscribers;

  private List<String> bannedUsers;

  public void addBannedUser(String id){
    bannedUsers.add(id);
    if(!(bannedUsers.contains(id))){
      bannedUsers.add(id);
    }
  }
  public void removeBannedUser(String id) {
    bannedUsers.remove(id);
  }
}
