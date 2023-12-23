package com.app.gamereview.service;

import com.app.gamereview.dto.request.game.*;
import com.app.gamereview.dto.response.game.GameDetailResponseDto;
import com.app.gamereview.dto.response.game.GetGameListResponseDto;
import com.app.gamereview.dto.response.tag.AddGameTagResponseDto;
import com.app.gamereview.dto.response.tag.GetAllTagsOfGameResponseDto;
import com.app.gamereview.enums.ForumType;
import com.app.gamereview.enums.TagType;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

	private final GameRepository gameRepository;
	private final UserRepository userRepository;
	private final TagRepository tagRepository;

	private final ForumRepository forumRepository;

	private final ProfileRepository profileRepository;

	private final MongoTemplate mongoTemplate;
	private final ModelMapper modelMapper;

	@Autowired
	public GameService(
			GameRepository gameRepository, UserRepository userRepository,
			MongoTemplate mongoTemplate, TagRepository tagRepository, ForumRepository forumRepository,
			ProfileRepository profileRepository, ModelMapper modelMapper) {
		this.gameRepository = gameRepository;
		this.userRepository = userRepository;
		this.tagRepository = tagRepository;
		this.mongoTemplate = mongoTemplate;
		this.forumRepository = forumRepository;
		this.profileRepository = profileRepository;
		this.modelMapper = modelMapper;

		modelMapper.addMappings(new PropertyMap<Game, GameDetailResponseDto>() {
			@Override
			protected void configure() {
				skip().setDeveloper(null);
				skip().setDuration(null);
				skip().setArtStyles(null);
				skip().setGenre(null);
				skip().setOtherTags(null);
				skip().setPlatforms(null);
				skip().setPlayerTypes(null);
				skip().setProduction(null);
			}
		});
	}

	public List<Game> getGames(GetGameListRequestDto filter) {
		Query query = new Query();
		if(filter != null) {
			if (filter.getFindDeleted() == null || !filter.getFindDeleted()) {
				query.addCriteria(Criteria.where("isDeleted").is(false));
			}
			if(filter.getGameName() != null && !filter.getGameName().isBlank()){
				query.addCriteria(Criteria.where("gameName").is(filter.getGameName()));
			}
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

		return mongoTemplate.find(query, Game.class);
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

		if(game.getAllTags().contains(tag.getId())){
			throw new BadRequestException("Tag is already added");
		}

		game.addTag(tag);
		gameRepository.save(game);

		AddGameTagResponseDto response = new AddGameTagResponseDto();
		response.setGameId(game.getId());
		response.setAddedTag(tag);
		return response;
	}

	public Boolean removeGameTag(RemoveGameTagRequestDto request){
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

		if(!game.getAllTags().contains(tag.getId())){
			return false;
		}

		game.removeTag(tag);
		gameRepository.save(game);

		return true;
	}

	public GameDetailResponseDto getGameDetail(String id){
		Optional<Game> optionalGame = gameRepository.findById(id);
		if (optionalGame.isPresent()) {
			Game game = optionalGame.get();
			GameDetailResponseDto response = modelMapper.map(game, GameDetailResponseDto.class);

			for(String tagId : game.getAllTags()){
				Optional<Tag> tag = tagRepository.findById(tagId);
				tag.ifPresent(response::populateTag);
			}

			return response;
		}
		else {
				throw new ResourceNotFoundException("Game not found");

		}
	}

	public GameDetailResponseDto getGameByName(String name){
		Optional<Game> optionalGame = gameRepository.findByGameNameAndIsDeletedFalse(name);
		if (optionalGame.isPresent()) {
			Game game = optionalGame.get();
			GameDetailResponseDto response = modelMapper.map(game, GameDetailResponseDto.class);

			for(String tagId : game.getAllTags()){
				Optional<Tag> tag = tagRepository.findById(tagId);
				tag.ifPresent(response::populateTag);
			}

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
				throw new ResourceNotFoundException("One of the given genre is not found.");
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

	public Game editGame(String id, UpdateGameRequestDto request){
		Optional<Game> findGame = gameRepository.findByIdAndIsDeletedFalse(id);

		if(findGame.isEmpty()){
			throw new ResourceNotFoundException("Game is not found");
		}

		Game gameToUpdate = findGame.get();
		gameToUpdate.setGameName(request.getGameName());
		gameToUpdate.setGameDescription(request.getGameDescription());
		gameToUpdate.setGameIcon(request.getGameIcon());
		gameToUpdate.setReleaseDate(request.getReleaseDate());
		gameToUpdate.setMinSystemReq(request.getMinSystemReq());
		gameRepository.save(gameToUpdate);

		return gameToUpdate;
	}

	public Boolean deleteGame(String id){
		Optional<Game> findGame = gameRepository.findByIdAndIsDeletedFalse(id);

		if(findGame.isEmpty()){
			throw new ResourceNotFoundException("Game is not found");
		}

		Game gameToDelete = findGame.get();
		gameToDelete.setIsDeleted(true);
		gameRepository.save(gameToDelete);

		return true;
	}

	public List<Game> getRecommendedGames(){
		Query query = new Query();	// all games except the base game
		query.addCriteria(Criteria.where("isDeleted").is(false));
		query.with(Sort.by(Sort.Direction.DESC, "overallVote"));
		query.limit(10);

		return mongoTemplate.find(query, Game.class);
	}

	public List<Game> getRecommendedGames(String email){

		if(email == null) throw new ResourceNotFoundException("User's token couldn't be validated");

		Optional<User> findUser = userRepository.findByEmailAndIsDeletedFalse(email);

		if(findUser.isEmpty()) throw new ResourceNotFoundException(
				"User with the token/email information couldn't be found");

		User user = findUser.get();

		Optional<Profile> findProfile = profileRepository.findByUserIdAndIsDeletedFalse(user.getId());

		if(findProfile.isEmpty()){
			throw new ResourceNotFoundException("Profile of the user is not found, unexpected error has occurred");
		}

		Profile profile = findProfile.get();
		List<String> followedGameIds = profile.getGames();

		TreeSet<RecommendGameDto> recommendedGames = new TreeSet<>(Comparator.reverseOrder());

		for(String gameId : followedGameIds){
			recommendedGames.addAll(recommendationByGameId(gameId));
		}

		List<Game> recommendations = new ArrayList<>();

		for(RecommendGameDto gameDto : recommendedGames){
			recommendations.add(gameDto.getGame());
			if(recommendations.size() >= 10){	// get only top 10 recommendations
				break;
			}
		}

		return recommendations;
	}

	public TreeSet<RecommendGameDto> recommendationByGameId(String gameId){
		Optional<Game> findGame = gameRepository.findByIdAndIsDeletedFalse(gameId);

		Set<String> idList = new HashSet<>();

		if(findGame.isEmpty()){
			throw new ResourceNotFoundException("Game is not found");
		}

		Game game = findGame.get();
		idList.add(game.getId());

		TreeSet<RecommendGameDto> scoreSet = new TreeSet<>(Comparator.reverseOrder());

		String[] words = game.getGameName().split(" ",-2);
		for(String word : words){
			if(word.length() > 3){
				String regexPattern = ".*" + word + ".*";
				Query query = new Query();
				query.addCriteria(Criteria.where("gameName").regex(regexPattern, "i"));
				query.addCriteria(Criteria.where("_id").ne(game.getId()));
				List<Game> similarNames = mongoTemplate.find(query, Game.class);
				for(Game i : similarNames){
					RecommendGameDto dto = new RecommendGameDto();
					dto.setGame(i);
					scoreSet.add(dto);
					idList.add(dto.getGame().getId());
				}
			}
		}

		// List<Game> recommendations = new ArrayList<>();

		Query allGamesQuery = new Query();	// all games except the base game
		allGamesQuery.addCriteria(Criteria.where("isDeleted").is(false));
		allGamesQuery.addCriteria(Criteria.where("_id").nin(idList));

		List<Game> allGames = mongoTemplate.find(allGamesQuery, Game.class);

		for(Game candidateGame : allGames){
			int score = calculateSimilarityScore(game, candidateGame);
			if(score != 0){
				RecommendGameDto dto = new RecommendGameDto();
				dto.setGame(candidateGame);
				dto.setScore(score);
				scoreSet.add(dto);
			}
		}

		return scoreSet;
	}

	public int calculateSimilarityScore(Game based, Game candidate){
		int score = 0;

		List<String> baseTags = based.getAllTags();
		baseTags.retainAll(candidate.getAllTags());

		for(String tagId : baseTags){
			Optional<Tag> findTag = tagRepository.findByIdAndIsDeletedFalse(tagId);
			if(findTag.isPresent()){
				score++;
				if(findTag.get().getTagType().equals(TagType.PRODUCTION)){
					score += 2;
				}
				else if(findTag.get().getTagType().equals(TagType.GENRE)){
					score += 4;
				}
			}
		}
		return score;
	}
}


