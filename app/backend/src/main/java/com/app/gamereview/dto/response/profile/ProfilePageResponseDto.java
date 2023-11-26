package com.app.gamereview.dto.response.profile;

import com.app.gamereview.dto.response.game.GameDetailResponseDto;
import com.app.gamereview.model.Achievement;
import com.app.gamereview.model.Review;
import com.app.gamereview.model.User;
import com.app.gamereview.model.Group;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfilePageResponseDto {

    private String id;

    private User user;

    private List<Achievement> achievements;

    private int reviewCount;

    private int voteCount;

    private int commentCount;

    private int postCount;

    private Boolean isReviewedYet;

    private Boolean isVotedYet;

    private Boolean isCommentedYet;

    private Boolean isPostedYet;

    private Boolean isPrivate;

    private String profilePhoto;

    private List<GameDetailResponseDto> games;

    private List<Review> reviews;

    private List<Group> groups;

    private String steamProfile;

    private String epicGamesProfile;

    private String xboxProfile;
}
