package com.app.gamereview.model;

import com.app.gamereview.model.common.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Game")
@Getter
@Setter
public class Game extends BaseModel {

	private String gameName;

	private String gameDescription;

	private String gameIcon;

	private Date releaseDate;

	private List<Tag> playerTypes;

	private List<Tag> genre;

	private Tag production;

	private Tag duration;

	private List<Tag> platforms;

	private List<Tag> artStyles;

	private Tag developer;

	private List<Tag> otherTags;

	private String minSystemReq;

	public Game(String gameName, String gameDescription, Date releaseDate, String minSystemReq) {
		this.gameName = gameName;
		this.gameDescription = gameDescription;
		this.releaseDate = releaseDate;
		this.minSystemReq = minSystemReq;
	}

	public void addTag(Tag tag){
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
