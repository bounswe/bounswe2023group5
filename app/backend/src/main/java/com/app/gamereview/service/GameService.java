package com.app.gamereview.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.gamereview.dto.request.game.CreateGameRequestDto;
import com.app.gamereview.dto.request.tag.AddGameTagRequestDto;
import com.app.gamereview.dto.response.tag.AddGameTagResponseDto;
import com.app.gamereview.dto.response.tag.GetAllTagsOfGameResponseDto;
import com.app.gamereview.enums.ForumType;
import com.app.gamereview.enums.TagType;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Forum;
import com.app.gamereview.model.Tag;
import com.app.gamereview.repository.ForumRepository;
import com.app.gamereview.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.app.gamereview.dto.request.game.GetGameListRequestDto;
import com.app.gamereview.dto.response.game.GetGameListResponseDto;
import com.app.gamereview.model.Game;
import com.app.gamereview.repository.GameRepository;
import com.app.gamereview.dto.response.game.GameDetailResponseDto;

@Service
public class GameService {

	private final GameRepository gameRepository;
	private final TagRepository tagRepository;

	private final ForumRepository forumRepository;

	private final MongoTemplate mongoTemplate;
	private final ModelMapper modelMapper;

	@Autowired
	public GameService(
			GameRepository gameRepository,
			MongoTemplate mongoTemplate,
			TagRepository tagRepository, ForumRepository forumRepository, ModelMapper modelMapper) {
		this.gameRepository = gameRepository;
		this.tagRepository = tagRepository;
		this.mongoTemplate = mongoTemplate;
		this.forumRepository = forumRepository;
		this.modelMapper = modelMapper;
	}

	public List<GetGameListResponseDto> getAllGames(GetGameListRequestDto filter) {
		Query query = new Query();
		if(filter != null) {
			if (filter.getFindDeleted() == null || !filter.getFindDeleted()) {
				query.addCriteria(Criteria.where("isDeleted").is(false));
			}
			if(filter.getSearch() != null && !filter.getSearch().isBlank()){
				query.addCriteria(Criteria.where("gameName").regex(filter.getSearch(), "i"));

			}
			else {
				if (filter.getPlayerTypes() != null && !filter.getPlayerTypes().isEmpty()) {
					query.addCriteria(Criteria.where("playerTypes").in(filter.getPlayerTypes()));
				}
				if (filter.getGenre() != null && !filter.getGenre().isEmpty()) {
					query.addCriteria(Criteria.where("genre").in(filter.getGenre()));
				}
				if (filter.getProduction() != null && !filter.getProduction().isBlank()) {
					query.addCriteria(Criteria.where("production").is(filter.getProduction()));
				}
				if (filter.getPlatform() != null && !filter.getPlatform().isEmpty()) {
					query.addCriteria(Criteria.where("platforms").in(filter.getPlatform()));
				}
				if (filter.getArtStyle() != null && !filter.getArtStyle().isEmpty()) {
					query.addCriteria(Criteria.where("artStyles").in(filter.getArtStyle()));
				}
			}
		}

		List<Game> gamesList = mongoTemplate.find(query, Game.class);

		return gamesList.stream().map(this::mapToGetGameListResponseDto).collect(Collectors.toList());
	}

	private GetGameListResponseDto mapToGetGameListResponseDto(Game game) {
        return new GetGameListResponseDto(game.getId(), game.getGameName(), game.getGameDescription(),
				game.getGameIcon());
	}

	public GetAllTagsOfGameResponseDto getGameTags(String gameId){
		Optional<Game> findGame = gameRepository.findById(gameId);

		if(findGame.isEmpty() || findGame.get().getIsDeleted()){
			throw new ResourceNotFoundException("Game does not exist");
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
			throw new ResourceNotFoundException("Game does not exist");
		}

		if(findTag.isEmpty() || findTag.get().getIsDeleted()){
			throw new ResourceNotFoundException("Tag does not exist");
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

	public GameDetailResponseDto getGameDetail(String id){
		Optional<Game> optionalGame = gameRepository.findById(id);
		if (optionalGame.isPresent()) {
			Game game = optionalGame.get();
			GameDetailResponseDto response = new GameDetailResponseDto();
			response.setGame(game);
			return response;
		}
		else {
				throw new ResourceNotFoundException("Game not found");

		}
	}

	public Game createGame(CreateGameRequestDto request){

		Optional<Game> sameName = gameRepository
				.findByGameNameAndIsDeletedFalse(request.getGameName());

		if (sameName.isPresent()) {
			throw new BadRequestException("Game with the same name already exist");
		}

		if (request.getProduction() != null) {
			Optional<Tag> production = tagRepository.findByIdAndIsDeletedFalse(request.getProduction());

			if (production.isEmpty() || production.get().getTagType() != TagType.PRODUCTION) {
				throw new ResourceNotFoundException("Given production is not found.");
			}
		}

		Optional<Tag> developer = tagRepository.findByIdAndIsDeletedFalse(request.getDeveloper());

		if (developer.isEmpty() || developer.get().getTagType() != TagType.DEVELOPER) {
			throw new ResourceNotFoundException("Given developer is not found.");
		}


		for (String playerTypeId : request.getPlayerTypes()) {
			Optional<Tag> playerType = tagRepository.findByIdAndIsDeletedFalse(playerTypeId);
			if (playerType.isEmpty() || playerType.get().getTagType() != TagType.PLAYER_TYPE) {
				throw new ResourceNotFoundException("One of the given player types is not found.");
			}
		}

		for (String genreId : request.getGenre()) {
			Optional<Tag> genre = tagRepository.findByIdAndIsDeletedFalse(genreId);
			if (genre.isEmpty() || genre.get().getTagType() != TagType.GENRE) {
				throw new ResourceNotFoundException("One of the givem genre is not found.");
			}
		}

		for (String platformId : request.getPlatforms()) {
			Optional<Tag> platform = tagRepository.findByIdAndIsDeletedFalse(platformId);
			if (platform.isEmpty() || platform.get().getTagType() != TagType.PLATFORM) {
				throw new ResourceNotFoundException("One of the given platforms is not found.");
			}
		}

		if (request.getArtStyles() != null) {
			for (String artstyleId : request.getArtStyles()) {
				Optional<Tag> artstyle = tagRepository.findByIdAndIsDeletedFalse(artstyleId);
				if (artstyle.isEmpty() || artstyle.get().getTagType() != TagType.ART_STYLE) {
					throw new ResourceNotFoundException("One of the given art styles is not found.");
				}
			}
		} else {
			request.setArtStyles(new ArrayList<>());
		}

		if (request.getOtherTags() != null) {
			for (String tagId : request.getOtherTags()) {
				Optional<Tag> tag = tagRepository.findByIdAndIsDeletedFalse(tagId);
				if (tag.isEmpty() || tag.get().getTagType() != TagType.OTHER) {
					throw new ResourceNotFoundException("One of the given tags is not found.");
				}
			}
		} else {
			request.setOtherTags(new ArrayList<>());
		}

		Game gameToCreate = modelMapper.map(request, Game.class);

		Forum correspondingForum = new Forum(request.getGameName(), ForumType.GAME, gameToCreate.getId(), new ArrayList<>(), new ArrayList<>());
		forumRepository.save(correspondingForum);

		gameToCreate.setForum(correspondingForum.getId());
		return gameRepository.save(gameToCreate);
	}


}
