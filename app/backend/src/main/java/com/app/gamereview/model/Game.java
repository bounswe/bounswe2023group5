package com.app.gamereview.model;

import com.app.gamereview.model.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Game")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseModel {

	private String gameName;

	private String gameDescription;

	private String gameIcon;

	private float overallRating;

	private float ratingCount;

	private Date releaseDate;

    private String forum;

	private List<String> playerTypes = new ArrayList<>();

	private List<String> genre = new ArrayList<>();

	private String production;

	private String duration;

	private List<String> platforms = new ArrayList<>();

	private List<String> artStyles = new ArrayList<>();

	private String developer;

	private List<String> otherTags = new ArrayList<>();

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
				playerTypes.add(tag.getId());
				break;
			case GENRE:
				genre.add(tag.getId());
				break;
			case PRODUCTION:
				production = tag.getId();
				break;
			case DURATION:
				duration = tag.getId();
				break;
			case PLATFORM:
				platforms.add(tag.getId());
				break;
			case ART_STYLE:
				artStyles.add(tag.getId());
				break;
			case DEVELOPER:
				developer = tag.getId();
				break;
			case OTHER:
				otherTags.add(tag.getId());
				break;
		}
	}

	public List<String> getAllTags(){
		List<String> allTags = new ArrayList<>();

		allTags.addAll(playerTypes);
		allTags.addAll(genre);
		allTags.addAll(platforms);
		allTags.addAll(artStyles);
		allTags.addAll(otherTags);
		allTags.add(production);
		allTags.add(duration);
		allTags.add(developer);

		return allTags;
	}

	public void addRating(float newRating){
		overallRating = (overallRating * ratingCount + newRating) / (ratingCount + 1);
		ratingCount += 1;
	}

	public void updateRating(float removedRating, float newRating){
		float nominator = overallRating * ratingCount;
		nominator += (newRating - removedRating);
		overallRating = nominator / ratingCount;
	}

	public void deleteRating(float deletedRating){
		if(ratingCount == 1){
			overallRating = 0;
			ratingCount -= 1;
		}
		else{
			float nominator = overallRating * ratingCount;
			nominator -= deletedRating;
			ratingCount -= 1;
			overallRating = nominator / ratingCount;
		}
	}


}
