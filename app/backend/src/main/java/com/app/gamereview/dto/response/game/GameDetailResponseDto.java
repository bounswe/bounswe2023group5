package com.app.gamereview.dto.response.game;

import com.app.gamereview.model.Game;
import com.app.gamereview.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class GameDetailResponseDto {

    private String id;

    private String gameName;

    private String gameDescription;

    private String gameIcon;

    private float overallRating;

    private float ratingCount;

    private Date releaseDate;

    private String forum;

    private List<Tag> playerTypes = new ArrayList<>();

    private List<Tag> genre = new ArrayList<>();

    private Tag production;

    private Tag duration;

    private List<Tag> platforms = new ArrayList<>();

    private List<Tag> artStyles = new ArrayList<>();

    private Tag developer;

    private List<Tag> otherTags = new ArrayList<>();

    private String minSystemReq;

    private LocalDateTime createdAt;

    private Boolean isDeleted;

    public void populateTag(Tag tag){
        switch (tag.getTagType()){
            case PLAYER_TYPE:
                playerTypes.add(tag);
                break;
            case GENRE:
                genre.add(tag);
                break;
            case PRODUCTION:
                production = tag;
                break;
            case DURATION:
                duration = tag;
                break;
            case PLATFORM:
                platforms.add(tag);
                break;
            case ART_STYLE:
                artStyles.add(tag);
                break;
            case DEVELOPER:
                developer = tag;
                break;
            case OTHER:
                otherTags.add(tag);
                break;
        }
    }
}
