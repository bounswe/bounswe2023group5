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
public class GetGroupDetailResponseDto {
    private String id;

    private String title;

    private String description;

    private MembershipPolicy membershipPolicy;

    private List<Tag> tags = new ArrayList<>(); // list of tags

    private String gameId;      // id of related game

    private String forumId;       // id of the forum of the group

    private int quota;

    private List<MemberInfo> moderators = new ArrayList<>();    // userIds of the moderators

    private List<MemberInfo> members = new ArrayList<>();       // userIds of the members

    private List<MemberInfo> bannedMembers = new ArrayList<>();       // userIds of the banned members

    private Boolean avatarOnly;

    private LocalDateTime createdAt;

    private Boolean userJoined;

    public static class MemberInfo {
        public String id;

        public String username;

        public String photoUrl;
    }
    public void populateTag(Tag tag){
        this.tags.add(tag);
    }
}
