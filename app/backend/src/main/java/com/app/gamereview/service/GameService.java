package com.app.gamereview.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.gamereview.dto.request.tag.AddGameTagRequestDto;
import com.app.gamereview.dto.response.tag.AddGameTagResponseDto;
import com.app.gamereview.dto.response.tag.GetAllTagsOfGameResponseDto;
import com.app.gamereview.model.Tag;
import com.app.gamereview.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.app.gamereview.dto.request.GetGameListRequestDto;
import com.app.gamereview.dto.response.GetGameListResponseDto;
import com.app.gamereview.model.Game;
import com.app.gamereview.repository.GameRepository;

@Service
public class GameService {

	private final GameRepository gameRepository;
	private final TagRepository tagRepository;

	private final MongoTemplate mongoTemplate;

	@Autowired
	public GameService(
			GameRepository gameRepository,
			MongoTemplate mongoTemplate,
			TagRepository tagRepository) {
		this.gameRepository = gameRepository;
		this.tagRepository = tagRepository;
		this.mongoTemplate = mongoTemplate;
	}

	public List<GetGameListResponseDto> getAllGames(GetGameListRequestDto filter) {
		Query query = new Query();
		if(filter != null) {
			if (filter.getFindDeleted() == null) {
				query.addCriteria(Criteria.where("isDeleted").is(false));
			} else if (filter.getFindDeleted() == false) {
				query.addCriteria(Criteria.where("isDeleted").is(false));
			}

			if (filter.getPlayerTypes() != null && filter.getPlayerTypes().size() > 0) {
				query.addCriteria(Criteria.where("playerType.name").all(filter.getPlayerTypes()));
			}
			if (filter.getGenre() != null && filter.getGenre().size() > 0) {
				query.addCriteria(Criteria.where("genre.name").all(filter.getGenre()));
			}
			if (filter.getProduction() != null && filter.getProduction().length() > 0) {
				query.addCriteria(Criteria.where("production.name").is(filter.getProduction()));
			}
			if (filter.getPlatform() != null && filter.getPlatform().size() > 0) {
				query.addCriteria(Criteria.where("platform.name").all(filter.getPlatform()));
			}
			if (filter.getArtStyle() != null && filter.getArtStyle().size() > 0) {
				query.addCriteria(Criteria.where("artStyle.name").all(filter.getArtStyle()));
			}
		}



		List<Game> gamesList = mongoTemplate.find(query, Game.class);

		return gamesList.stream().map(this::mapToGetGameListResponseDto).collect(Collectors.toList());
	}

	private GetGameListResponseDto mapToGetGameListResponseDto(Game game) {
		GetGameListResponseDto gameDto = new GetGameListResponseDto(game.getGameName(), game.getGameDescription(),
				game.getGameIcon());
		return gameDto;
	}

	public GetAllTagsOfGameResponseDto getGameTags(String gameId){
		Optional<Game> findGame = gameRepository.findById(gameId);

		if(findGame.isEmpty() || findGame.get().getIsDeleted()){
			// TODO exception
			return null;
		}

		Game game = findGame.get();

		GetAllTagsOfGameResponseDto response = new GetAllTagsOfGameResponseDto();

		response.setPlayerTypes(game.getPlayerTypes());
		response.setGenre(game.getGenre());
		response.setProduction(game.getProduction());
		response.setDuration(game.getDuration());
		response.setPlatforms(game.getPlatforms());
		response.setArtStyles(game.getArtStyles());
		response.setDeveloper(game.getDeveloper());
		response.setOtherTags(game.getOtherTags());

		return response;
	}

	public AddGameTagResponseDto addGameTag(AddGameTagRequestDto request){
		Optional<Game> findGame = gameRepository.findById(request.getGameId());

		Optional<Tag> findTag = tagRepository.findById(request.getTagId());

		if(findGame.isEmpty() || findGame.get().getIsDeleted()){
			// TODO exception
			return null;
		}

		if(findTag.isEmpty() || findTag.get().getIsDeleted()){
			// TODO exception
			return null;
		}

		Game game = findGame.get();
		Tag tag = findTag.get();
		game.addTag(tag);
		gameRepository.save(game);

		AddGameTagResponseDto response = new AddGameTagResponseDto();
		response.setGameId(game.getId());
		response.setAddedTag(tag);
		return response;
	}

}
