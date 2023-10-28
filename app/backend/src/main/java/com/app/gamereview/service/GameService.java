package com.app.gamereview.service;

import java.util.List;
import java.util.stream.Collectors;

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

	private final MongoTemplate mongoTemplate;

	@Autowired
	public GameService(GameRepository gameRepository, MongoTemplate mongoTemplate) {
		this.gameRepository = gameRepository;
		this.mongoTemplate = mongoTemplate;
	}

	public List<GetGameListResponseDto> getAllGames(GetGameListRequestDto filter) {
		Query query = new Query();

		if (filter.getFindDeleted() == null) {
			query.addCriteria(Criteria.where("isDeleted").is(false));
		}
		else if (filter.getFindDeleted() == false) {
			query.addCriteria(Criteria.where("isDeleted").is(false));
		}

		List<Game> gamesList = mongoTemplate.find(query, Game.class);

		return gamesList.stream().map(this::mapToGetGameListResponseDto).collect(Collectors.toList());
	}

	private GetGameListResponseDto mapToGetGameListResponseDto(Game game) {
		GetGameListResponseDto gameDto = new GetGameListResponseDto(game.getGameName(), game.getGameDescription(),
				game.getGameIcon());
		return gameDto;
	}

}
