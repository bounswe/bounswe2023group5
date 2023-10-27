package com.app.gamereview.model;

import com.app.gamereview.model.common.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "Game")
@Getter
@Setter
public class Game extends BaseModel {

    //TODO adding fields that depend on tag entity

    private String gameName;

    private String gameDescription;

    private String gameIcon;

    private Date releaseDate;

    private String minSystemReq;

    public Game(String gameName, String gameDescription, Date releaseDate, String minSystemReq) {
        this.gameName = gameName;
        this.gameDescription = gameDescription;
        this.releaseDate = releaseDate;
        this.minSystemReq = minSystemReq;
    }
}
