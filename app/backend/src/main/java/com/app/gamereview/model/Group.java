package com.app.gamereview.model;

import com.app.gamereview.enums.MembershipPolicy;
import com.app.gamereview.model.common.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Group")
@TypeAlias("Group")
@Getter
@Setter
public class Group extends BaseModel {
    private String title;

    private String description;

    private MembershipPolicy membershipPolicy;

    private List<String> tags = new ArrayList<>(); // list of tag ids

    private String gameId;      // id of related game

    private String forumId;       // id of the forum of the group

    private int quota;

    private List<String> moderators = new ArrayList<>();    // userIds of the moderators

    private List<String> members = new ArrayList<>();       // userIds of the members

    private List<String> bannedMembers = new ArrayList<>();       // userIds of the banned members

    private Boolean avatarOnly;

    public void addMember(String id){
        members.add(id);
    }

    public void removeMember(String id){
        members.remove(id);
    }

    public void addBannedUser(String id){
        members.remove(id);
        bannedMembers.add(id);
    }

    public void addModerator(String id){
        moderators.add(id);
        if(!members.contains(id)){
            members.add(id);
        }
    }
    public void removeModerator(String id){
        moderators.remove(id);
    }


    public void addTag(Tag tag){
        this.tags.add(tag.getId());
    }

    public void removeTag(Tag tag){
        this.tags.remove(tag.getId());
    }
}
