package com.app.gamereview.dto.response.group;

import com.app.gamereview.enums.MembershipPolicy;
import com.app.gamereview.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class GetGroupResponseDto {
    private String id;

    private String title;

    private String description;

    private MembershipPolicy membershipPolicy;

    private List<Tag> tags = new ArrayList<>(); // list of tags

    private String gameId;      // id of related game

    private String forumId;       // id of the forum of the group

    private int quota;

    private List<String> moderators = new ArrayList<>();    // userIds of the moderators

    private List<String> members = new ArrayList<>();       // userIds of the members

    private List<String> bannedMembers = new ArrayList<>();       // userIds of the banned members

    private Boolean avatarOnly;

    private LocalDateTime createdAt;

    public void populateTag(Tag tag){
        this.tags.add(tag);
    }
}
