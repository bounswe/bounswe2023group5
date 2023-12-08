package com.app.gamereview.model;

import com.app.gamereview.model.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "Profile")
@TypeAlias("Profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends BaseModel {
    private String userId;      // user the profile is linked to

    private List<String> achievements = new ArrayList<>();  // ids of achievements

    private int reviewCount = 0;

    private int voteCount = 0;

    private int commentCount = 0;

    private int postCount = 0;

    private Boolean isReviewedYet = false;

    private Boolean isVotedYet = false;

    private Boolean isCommentedYet = false;

    private Boolean isPostedYet = false;

    private Boolean isPrivate = false;

    private String profilePhoto;

    private List<String> games = new ArrayList<>();  // ids of games

    private String steamProfile;

    private String epicGamesProfile;

    private String xboxProfile;

    public void addAchievement(String achievementId) {
        if (!achievements.contains(achievementId)) {
            achievements.add(achievementId);
        }
    }

    public void addGame(String gameId) {
        if (!games.contains(gameId)) {
            games.add(gameId);
        }
    }

    public void removeGame(String gameId) {
        games.remove(gameId);
    }
}
