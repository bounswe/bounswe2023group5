package com.app.gamereview.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.request.home.HomePagePostsFilterRequestDto;
import com.app.gamereview.dto.response.comment.CommentReplyResponseDto;
import com.app.gamereview.dto.response.comment.GetPostCommentsResponseDto;
import com.app.gamereview.dto.response.home.HomePagePostResponseDto;
import com.app.gamereview.dto.response.post.GetPostDetailResponseDto;
import com.app.gamereview.enums.*;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.model.*;
import com.app.gamereview.model.Character;
import com.app.gamereview.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.app.gamereview.dto.request.post.CreatePostRequestDto;
import com.app.gamereview.dto.request.post.EditPostRequestDto;
import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.exception.ResourceNotFoundException;

@Service
public class PostService {

    private final PostRepository postRepository;

    private final ForumRepository forumRepository;

    private final GameRepository gameRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    private final VoteRepository voteRepository;

    private final AchievementRepository achievementRepository;

    private final CharacterRepository characterRepository;

    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;


    @Autowired
    public PostService(PostRepository postRepository, ForumRepository forumRepository, UserRepository userRepository,
                       ProfileRepository profileRepository, TagRepository tagRepository,
                       CommentRepository commentRepository, VoteRepository voteRepository,
                       AchievementRepository achievementRepository, GameRepository gameRepository, CharacterRepository characterRepository, MongoTemplate mongoTemplate,
                       NotificationService notificationService, GroupRepository groupRepository,
                       ModelMapper modelMapper) {

        this.postRepository = postRepository;
        this.forumRepository = forumRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
        this.achievementRepository = achievementRepository;
        this.characterRepository = characterRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
        this.notificationService = notificationService;
        this.groupRepository = groupRepository;

        modelMapper.addMappings(new PropertyMap<Post, HomePagePostResponseDto>() {
            @Override
            protected void configure() {
                skip().setTags(null); // Exclude tags from mapping
            }
        });
    }

    public List<GetPostListResponseDto> getPostList(GetPostListFilterRequestDto filter, String email) {
        Optional<User> loggedInUser = userRepository.findByEmailAndIsDeletedFalse(email);
        String loggedInUserId = loggedInUser.map(User::getId).orElse(null);

        Query query = new Query();

        query.addCriteria(Criteria.where("forum").is(filter.getForum()));

        if (filter.getFindDeleted() == null || !filter.getFindDeleted()) {
            query.addCriteria(Criteria.where("isDeleted").is(false));
        }
        if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
            query.addCriteria(Criteria.where("title").regex(filter.getSearch(), "i"));
        }
        if (filter.getTags() != null && !filter.getTags().isEmpty()) {
            query.addCriteria(Criteria.where("tags").in(filter.getTags()));
        }
        if (filter.getSortBy() != null) {
            Sort.Direction sortDirection = Sort.Direction.DESC; // Default sorting direction (you can change it to ASC if needed)
            if (filter.getSortDirection() != null) {
                sortDirection = filter.getSortDirection().equals(SortDirection.ASCENDING.name()) ? Sort.Direction.ASC : Sort.Direction.DESC;
            }

            if (filter.getSortBy().equals(SortType.CREATION_DATE.name())) {
                query.with(Sort.by(sortDirection, "createdAt"));
            } else if (filter.getSortBy().equals(SortType.EDIT_DATE.name())) {
                query.with(Sort.by(sortDirection, "lastEditedAt"));
            } else if (filter.getSortBy().equals(SortType.OVERALL_VOTE.name())) {
                query.with(Sort.by(sortDirection, "overallVote"));
            } else if (filter.getSortBy().equals(SortType.VOTE_COUNT.name())) {
                query.with(Sort.by(sortDirection, "voteCount"));
            }
        }

        List<Post> postList = mongoTemplate.find(query, Post.class);

        return postList.stream().map(post -> mapToGetPostListResponseDto(post, loggedInUserId)).collect(Collectors.toList());
    }

    private GetPostListResponseDto mapToGetPostListResponseDto(Post post, String loggedInUserId) {
        Boolean isEdited = post.getCreatedAt().isBefore(post.getLastEditedAt());
        String posterId = post.getPoster();
        Optional<User> poster = userRepository.findByIdAndIsDeletedFalse(posterId);

        User posterObject = poster.orElse(null);
        int commentCount = commentRepository.countByPost(post.getId());

        Optional<Vote> userVote = voteRepository.findByTypeIdAndVotedBy(post.getId(), loggedInUserId);

        VoteChoice userVoteChoice = userVote.map(Vote::getChoice).orElse(null);

        List<Tag> tags = new ArrayList<>();

        Optional<Achievement> postAchievementOptional = achievementRepository.findByIdAndIsDeletedFalse(post.getAchievement());
        Achievement postAchievement = postAchievementOptional.orElse(null);

        Optional<Character> postCharacterOptional = characterRepository.findByIdAndIsDeletedFalse(post.getCharacter());
        Character postCharacter = postCharacterOptional.orElse(null);

        // Fetch tags individually
        for (String tagId : post.getTags()) {
            Optional<Tag> tag = tagRepository.findById(tagId);
            tag.ifPresent(tags::add);
        }

        return new GetPostListResponseDto(post.getId(), post.getTitle(), post.getPostContent(),
                posterObject, userVoteChoice, post.getPostImage(), postAchievement, postCharacter, post.getLastEditedAt(), post.getCreatedAt(), isEdited,
                tags, post.getInappropriate(), post.getOverallVote(), post.getVoteCount(), commentCount);
    }


    public GetPostDetailResponseDto getPostById(String id, String email) {
        Optional<Post> post = postRepository.findById(id);
        Optional<User> loggedInUser = userRepository.findByEmailAndIsDeletedFalse(email);
        String loggedInUserId = loggedInUser.map(User::getId).orElse(null);

        if (post.isEmpty()) {
            throw new ResourceNotFoundException("The post with the given id was not found");
        }
        Optional<Forum> forum = forumRepository.findById(post.get().getForum());
        if (forum.isPresent()) {
            if(loggedInUserId != null){
                List<String> bannedUsers = forum.get().getBannedUsers();
                if (bannedUsers.contains(loggedInUserId)) {
                    throw new ResourceNotFoundException("You cannot see the post because you are banned.");
                }
            }
        }

        GetPostDetailResponseDto postDto = modelMapper.map(post, GetPostDetailResponseDto.class);

        Optional<Vote> userVote = voteRepository.findByTypeIdAndVotedBy(id, loggedInUserId);

        VoteChoice userVoteChoice = userVote.map(Vote::getChoice).orElse(null);

        postDto.setUserVote(userVoteChoice);

        Optional<Achievement> postAchievement = achievementRepository.findByIdAndIsDeletedFalse(post.get().getAchievement());

        postAchievement.ifPresent(postDto::setAchievement);

        Optional<Character> postCharacter = characterRepository.findByIdAndIsDeletedFalse(post.get().getCharacter());

        postCharacter.ifPresent(postDto::setCharacter);

        List<Tag> tags = new ArrayList<>();

        // Fetch tags individually
        for (String tagId : post.get().getTags()) {
            Optional<Tag> tag = tagRepository.findById(tagId);
            tag.ifPresent(tags::add);
        }

        postDto.setTags(tags);

        Optional<User> poster = userRepository.findByIdAndIsDeletedFalse(post.get().getPoster());
        poster.ifPresent(postDto::setPoster);

        return postDto;
    }

    public List<GetPostCommentsResponseDto> getCommentList(String postId, String email) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> loggedInUser = userRepository.findByEmailAndIsDeletedFalse(email);
        String loggedInUserId = loggedInUser.map(User::getId).orElse(null);

        if (post.isEmpty()) {
            throw new ResourceNotFoundException("The post with the given id was not found");
        }
        Optional<Forum> forum = forumRepository.findById(post.get().getForum());
        if (forum.isPresent()) {
            if(loggedInUserId != null){
                List<String> bannedUsers = forum.get().getBannedUsers();
                if (bannedUsers.contains(loggedInUserId)) {
                    throw new ResourceNotFoundException("You cannot see the post because you are banned.");
                }
            }
        }
        List<Comment> comments = commentRepository.findByPost(postId);

        Map<String, GetPostCommentsResponseDto> commentMap = new HashMap<>();
        List<GetPostCommentsResponseDto> topLevelComments = new ArrayList<>();

        // First, convert all comments to DTOs and identify top-level comments
        for (Comment comment : comments) {
            GetPostCommentsResponseDto dto = convertToCommentDto(comment, loggedInUserId);
            commentMap.put(comment.getId(), dto);

            if (comment.getParentComment() == null) {
                topLevelComments.add(dto);
            }
        }

        // Next, associate replies with their parent comments
        for (Comment comment : comments) {
            if (comment.getParentComment() != null) {
                GetPostCommentsResponseDto parentDto = commentMap.get(comment.getParentComment());
                if (parentDto != null) {
                    parentDto.getReplies().add(convertToReplyDto(comment, loggedInUserId));
                }
            }
        }

        return topLevelComments;
    }

    private GetPostCommentsResponseDto convertToCommentDto(Comment comment, String loggedInUserId) {
        Boolean isEdited = comment.getCreatedAt().isBefore(comment.getLastEditedAt());
        String commenterId = comment.getCommenter();
        Optional<User> commenter = userRepository.findByIdAndIsDeletedFalse(commenterId);

        Optional<Vote> userVote = voteRepository.findByTypeIdAndVotedBy(comment.getId(), loggedInUserId);
        VoteChoice userVoteChoice = userVote.map(Vote::getChoice).orElse(null);

        User commenterObject = commenter.orElse(null);
        return new GetPostCommentsResponseDto(comment.getId(), comment.getCommentContent(), commenterObject,
                comment.getPost(), comment.getLastEditedAt(), comment.getCreatedAt(), isEdited, comment.getIsDeleted(), comment.getOverallVote(),
                comment.getVoteCount(), new ArrayList<>(), userVoteChoice);
    }

    private CommentReplyResponseDto convertToReplyDto(Comment comment, String loggedInUserId) {
        Boolean isEdited = comment.getCreatedAt().isBefore(comment.getLastEditedAt());
        String commenterId = comment.getCommenter();
        Optional<User> commenter = userRepository.findByIdAndIsDeletedFalse(commenterId);

        Optional<Vote> userVote = voteRepository.findByTypeIdAndVotedBy(comment.getId(), loggedInUserId);
        VoteChoice userVoteChoice = userVote.map(Vote::getChoice).orElse(null);

        User commenterObject = commenter.orElse(null);
        return new CommentReplyResponseDto(comment.getId(), comment.getCommentContent(), commenterObject,
                comment.getPost(), comment.getLastEditedAt(), comment.getCreatedAt(), isEdited, comment.getIsDeleted(), comment.getOverallVote(),
                comment.getVoteCount(), userVoteChoice);
    }


    public Post createPost(CreatePostRequestDto request, User user) {

        Optional<Forum> optionalForum = forumRepository.findById(request.getForum());

        if (optionalForum.isEmpty()) {
            throw new ResourceNotFoundException("Forum is not found.");
        }

        Forum forum = optionalForum.get();

        if(request.getAchievement() != null){   // if achievement is assigned
            Optional<Achievement> achievementOptional = achievementRepository.findById(request.getAchievement());

            if (achievementOptional.isEmpty()) {
                throw new ResourceNotFoundException("Achievement is not found.");
            }
        }

        if(request.getCharacter() != null){   // if character is assigned
            Optional<Character> characterOptional = characterRepository.findById(request.getCharacter());

            if (characterOptional.isEmpty()) {
                throw new ResourceNotFoundException("Character is not found.");
            }
        }

        if (request.getTags() != null) {
            for (String tagId : request.getTags()) {
                Optional<Tag> tag = tagRepository.findById(tagId);
                if (tag.isEmpty()) {
                    throw new ResourceNotFoundException("One of the tags with the given Id is not found.");
                }
            }
        } else {
            request.setTags(new ArrayList<>());
        }


        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        Profile profile = mongoTemplate.findOne(query, Profile.class);

        if(profile == null){
            throw new ResourceNotFoundException("Profile of the user does not exist");
        }

        if(!profile.getIsPostedYet()){      // first post of the user
            Optional<Achievement> achievement =
                    achievementRepository.findByIdAndIsDeletedFalse("88f359cc-8ca3-4286-bc13-1b44262ee9f4");
            achievement.ifPresent(value -> profile.addAchievement(value.getId()));
            profile.setIsPostedYet(true);
            CreateNotificationRequestDto createNotificationRequestDto= new CreateNotificationRequestDto();
            String message = NotificationMessage.FIRST_POST_ACHIEVEMENT.getMessageTemplate()
                    .replace("{user_name}", user.getUsername())
                    .replace("{forum_name}", forum.getName());
            createNotificationRequestDto.setMessage(message);
            createNotificationRequestDto.setParentType(NotificationParent.ACHIEVEMENT);
            createNotificationRequestDto.setUser(user.getId());
            notificationService.createNotification(createNotificationRequestDto);
        }

        profile.setPostCount(profile.getPostCount() + 1);
        profileRepository.save(profile);

        Post postToCreate = modelMapper.map(request, Post.class);
        postToCreate.setPoster(user.getId());
        postToCreate.setLastEditedAt(postToCreate.getCreatedAt());
        postToCreate.setInappropriate(false);
        postToCreate.setLocked(false);
        Post savedPost = postRepository.save(postToCreate);

        if(forum.getType() == ForumType.GROUP){
            Group group = mongoTemplate.findById(forum.getParent(), Group.class);
            if(group != null){
                if(group.getMembershipPolicy() == MembershipPolicy.PRIVATE) {
                    // send notification to group members
                    List<String> members = group.getMembers();
                    for(String member : members){
                        if(!member.equals(user.getId())){
                            Optional<User> userOptional = userRepository.findById(member);
                            if(userOptional.isPresent()){
                                User memberUser = userOptional.get();
                                CreateNotificationRequestDto createNotificationRequestDto = new CreateNotificationRequestDto();
                                String message = NotificationMessage.NEW_POST_IN_PRIVATE_GROUP.getMessageTemplate()
                                        .replace("{user_name}", memberUser.getUsername())
                                        .replace("{group_title}", group.getTitle());
                                createNotificationRequestDto.setMessage(message);
                                createNotificationRequestDto.setParentType(NotificationParent.POST);
                                createNotificationRequestDto.setParent(savedPost.getId());
                                createNotificationRequestDto.setUser(member);
                                notificationService.createNotification(createNotificationRequestDto);                            }
                        }
                    }
                }
            }
        }
        return savedPost;
    }

    public Post editPost(String id, EditPostRequestDto request, User user) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty() || post.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The post with the given id is not found.");
        }

        if (!post.get().getPoster().equals(user.getId())) {
            throw new BadRequestException("Only the user that created the post can edit it.");
        }

        Post editedPost = post.get();
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            editedPost.setTitle(request.getTitle());
        }

        if (request.getPostContent() != null && !request.getPostContent().isEmpty()) {
            editedPost.setPostContent(request.getPostContent());
        }

        if ((request.getPostContent() != null && !request.getPostContent().isEmpty()) || (request.getTitle() != null && !request.getTitle().isEmpty())) {
            editedPost.setLastEditedAt(LocalDateTime.now());
        }

        return postRepository.save(editedPost);
    }

    public Post deletePost(String id, User user) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty() || post.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The post with the given id is not found.");
        }

        if (!(post.get().getPoster().equals(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the user that created the post or the admin can delete it.");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(user.getId()));
        Profile profile = mongoTemplate.findOne(query, Profile.class);
        if(profile == null){
            throw new ResourceNotFoundException("Profile of the user does not exist");
        }
        profile.setPostCount(profile.getPostCount() - 1);
        profileRepository.save(profile);

        Post postToDelete = post.get();

        postToDelete.setIsDeleted(true);

        return postRepository.save(postToDelete);
    }

    public List<Post> getUserPostList(User user) {

        Query query = new Query();

        query.addCriteria(Criteria.where("poster").is(user.getId()));

        Sort.Direction sortDirection = Sort.Direction.DESC; // Default sorting direction (you can change it to ASC if needed)

        query.with(Sort.by(sortDirection, "createdAt"));


        return mongoTemplate.find(query, Post.class);

    }

    public List<HomePagePostResponseDto> getHomepagePosts(HomePagePostsFilterRequestDto filter, String email){
        if(email == null){
            return getHomePagePostsOfGuest(filter);
        }
        Optional<User> findUser = userRepository.findByEmailAndIsDeletedFalse(email);

        if(findUser.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }

        return getHomePagePostsOfUser(filter, findUser.get());
    }

    public List<HomePagePostResponseDto> getHomePagePostsOfGuest(HomePagePostsFilterRequestDto filter){
        Query query = new Query();
        query.addCriteria(Criteria.where("type").is(ForumType.GAME.name()));
        List<Forum> gameForums = mongoTemplate.find(query, Forum.class);
        List<Post> postsToShow = new ArrayList<>();

        Query allGamesQuery = new Query();
        allGamesQuery.addCriteria(Criteria.where("isDeleted").is(false));
        allGamesQuery.addCriteria(Criteria.where("isPromoted").is(true));
        List<Game> promotedGames = mongoTemplate.find(allGamesQuery, Game.class);
        int promotedCount = 0;
        if(promotedGames.size() >= 2){
            Collections.shuffle(promotedGames);
            Game randomGame1 = promotedGames.get(0);
            Game randomGame2 = promotedGames.get(1);
            List<Post> game1Posts =  postRepository.findByForumAndIsDeletedFalse(randomGame1.getForum());
            List<Post> game2Posts =  postRepository.findByForumAndIsDeletedFalse(randomGame2.getForum());
            Collections.shuffle(game1Posts);
            Collections.shuffle(game2Posts);
            if(!game1Posts.isEmpty()){
                postsToShow.add(game1Posts.get(0));
                promotedCount++;
            }
            if(!game2Posts.isEmpty()){
                postsToShow.add(game2Posts.get(0));
                promotedCount++;
            }
        }else if(promotedGames.size() == 1){
            Game promotedGame = promotedGames.get(0);
            List<Post> posts =  postRepository.findByForumAndIsDeletedFalse(promotedGame.getForum());
            Collections.shuffle(posts);
            if(!posts.isEmpty()){
                postsToShow.add(posts.get(0));
                postsToShow.add(posts.get(1));
                promotedCount= promotedCount+2;
            }
        }

        for(Forum forum : gameForums){
            postsToShow.addAll(postRepository.findByForumAndIsDeletedFalse(forum.getId()));
        }

        if(filter.getSortBy().equals(SortType.CREATION_DATE.name())){
            if(filter.getSortDirection().equals(SortDirection.DESCENDING.name())){
                // descending
                postsToShow.sort(Comparator.comparing(Post::getCreatedAt, Comparator.reverseOrder()));
            }
            else{
                // ascending
                postsToShow.sort(Comparator.comparing(Post::getCreatedAt));
            }
        }
        else if(filter.getSortBy().equals(SortType.OVERALL_VOTE.name())){
            if(filter.getSortDirection().equals(SortDirection.DESCENDING.name())){
                // descending
                postsToShow.sort(Comparator.comparing(Post::getOverallVote, Comparator.reverseOrder()));
            }
            else{
                // ascending
                postsToShow.sort(Comparator.comparing(Post::getOverallVote));
            }
        }
        else if(filter.getSortBy().equals(SortType.VOTE_COUNT.name())){
            if(filter.getSortDirection().equals(SortDirection.DESCENDING.name())){
                // descending
                postsToShow.sort(Comparator.comparing(Post::getVoteCount, Comparator.reverseOrder()));
            }
            else{
                // ascending
                postsToShow.sort(Comparator.comparing(Post::getVoteCount));
            }
        }

        List<Post> first20 = postsToShow.subList(0, Math.min(20, postsToShow.size()));

        List<HomePagePostResponseDto> first20dto = new ArrayList<>();
        int index = 0;
        for(Post post : first20){
            HomePagePostResponseDto dto = modelMapper.map(post,HomePagePostResponseDto.class);

            Optional<Forum> findForum = forumRepository.findByIdAndIsDeletedFalse(post.getForum());

            if(findForum.isEmpty()){
                continue;
            }

            Forum forumOfPost = findForum.get();

            dto.setType(forumOfPost.getType());
            dto.setTypeId(forumOfPost.getParent());

            String typeName = null;

            if(forumOfPost.getType().equals(ForumType.GROUP)){
                Optional<Group> findGroup = groupRepository.findByIdAndIsDeletedFalse(forumOfPost.getParent());

                if(findGroup.isEmpty()){
                    throw new ResourceNotFoundException("Group not found");
                }

                typeName = findGroup.get().getTitle();
            }

            else if(forumOfPost.getType().equals(ForumType.GAME)){
                Optional<Game> findGame = gameRepository.findByIdAndIsDeletedFalse(forumOfPost.getParent());

                if(findGame.isEmpty()){
                    throw new ResourceNotFoundException("Game not found");
                }

                typeName = findGame.get().getGameName();
            }

            Optional<User> findPoster = userRepository.findByIdAndIsDeletedFalse(post.getPoster());
            findPoster.ifPresent(dto::setPoster);

            Optional<Achievement> findAchievement = achievementRepository.findByIdAndIsDeletedFalse(post.getAchievement());
            findAchievement.ifPresent(dto::setAchievement);

            Optional<Character> findCharacter = characterRepository.findByIdAndIsDeletedFalse(post.getCharacter());
            findCharacter.ifPresent(dto::setCharacter);

            dto.setUserVote(null);

            dto.setTypeName(typeName);

            if(index<=promotedCount){
                dto.setIsPromoted(true);
            }else {
                dto.setIsPromoted(false);
            }
            index++;
            first20dto.add(dto);
        }

        return first20dto;
    }

    public List<HomePagePostResponseDto> getHomePagePostsOfUser(HomePagePostsFilterRequestDto filter, User user){
        Optional<Profile> findProfile = profileRepository.findByUserIdAndIsDeletedFalse(user.getId());

        if(findProfile.isEmpty()) {
            throw new ResourceNotFoundException("User doesn't have any profile, unexpected error has occurred");
        }

        Profile profileOfUser = findProfile.get();
        List<String> gameIds = profileOfUser.getGames();

        List<Post> postsToShow = new ArrayList<>();

        // get game posts (games that user follows)
        for(String gameId : gameIds){
            Optional<Game> findGame = gameRepository.findByIdAndIsDeletedFalse(gameId);

            if(findGame.isEmpty()){
                continue;
            }

            String forumId = findGame.get().getForum();

            postsToShow.addAll(postRepository.findByForumAndIsDeletedFalse(forumId));
        }

        // get group posts (groups that user is a member of)
        Query query = new Query();
        query.addCriteria(Criteria.where("members").in(user.getId()));
        List<Group> groupsUserIsMemberOf = mongoTemplate.find(query, Group.class);

        for(Group group : groupsUserIsMemberOf){
            postsToShow.addAll(postRepository.findByForumAndIsDeletedFalse(group.getForumId()));
        }

        if(filter.getSortBy().equals(SortType.CREATION_DATE.name())){
            if(filter.getSortDirection().equals(SortDirection.DESCENDING.name())){
                // descending
                postsToShow.sort(Comparator.comparing(Post::getCreatedAt, Comparator.reverseOrder()));
            }
            else{
                // ascending
                postsToShow.sort(Comparator.comparing(Post::getCreatedAt));
            }
        }
        else if(filter.getSortBy().equals(SortType.OVERALL_VOTE.name())){
            if(filter.getSortDirection().equals(SortDirection.DESCENDING.name())){
                // descending
                postsToShow.sort(Comparator.comparing(Post::getOverallVote, Comparator.reverseOrder()));
            }
            else{
                // ascending
                postsToShow.sort(Comparator.comparing(Post::getOverallVote));
            }
        }
        else if(filter.getSortBy().equals(SortType.VOTE_COUNT.name())){
            if(filter.getSortDirection().equals(SortDirection.DESCENDING.name())){
                // descending
                postsToShow.sort(Comparator.comparing(Post::getVoteCount, Comparator.reverseOrder()));
            }
            else{
                // ascending
                postsToShow.sort(Comparator.comparing(Post::getVoteCount));
            }
        }

        if(postsToShow.size() < 20){    // complete to 20
            Query postQuery = new Query();
            postQuery.addCriteria(Criteria.where("isDeleted").is(false));
            postQuery.with(Sort.by(Sort.Order.desc("overallVote")));
            List<Post> allPosts = mongoTemplate.find(postQuery,Post.class);
            int toAdd = 20 - postsToShow.size();
            for(Post post : allPosts){
                if(!postsToShow.contains(post)){
                    postsToShow.add(post);
                    toAdd--;
                }
                if(toAdd == 0)
                    break;
            }
        }

        List<HomePagePostResponseDto> first20dto = new ArrayList<>();

        Query allGamesQuery = new Query();
        allGamesQuery.addCriteria(Criteria.where("isDeleted").is(false));
        allGamesQuery.addCriteria(Criteria.where("isPromoted").is(true));
        List<Game> promotedGames = mongoTemplate.find(allGamesQuery, Game.class);
        int promotedCount = 0;
        if(promotedGames.size() >= 2){
            Collections.shuffle(promotedGames);
            Game randomGame1 = promotedGames.get(0);
            Game randomGame2 = promotedGames.get(1);
            List<Post> game1Posts =  postRepository.findByForumAndIsDeletedFalse(randomGame1.getForum());
            List<Post> game2Posts =  postRepository.findByForumAndIsDeletedFalse(randomGame2.getForum());
            Collections.shuffle(game1Posts);
            Collections.shuffle(game2Posts);
            if(!game1Posts.isEmpty()){
                postsToShow.add(0,game1Posts.get(0));
                promotedCount++;
            }
            if(!game2Posts.isEmpty()){
                postsToShow.add(0,game2Posts.get(0));
                promotedCount++;
            }
        }else if(promotedGames.size() == 1){
            Game promotedGame = promotedGames.get(0);
            List<Post> posts =  postRepository.findByForumAndIsDeletedFalse(promotedGame.getForum());
            Collections.shuffle(posts);
            if(!posts.isEmpty()){
                postsToShow.add(0,posts.get(0));
                postsToShow.add(0,posts.get(1));
                promotedCount = promotedCount +2;

            }
        }
        List<Post> first20 = postsToShow.subList(0, Math.min(20, postsToShow.size()));
        int index = 0;
        for(Post post : first20){
            HomePagePostResponseDto dto = modelMapper.map(post,HomePagePostResponseDto.class);
            dto.setTags(populatedTags(post.getTags()));

            Optional<Forum> findForum = forumRepository.findByIdAndIsDeletedFalse(post.getForum());

            if(findForum.isEmpty()){
                continue;
            }

            Forum forumOfPost = findForum.get();

            dto.setType(forumOfPost.getType());
            dto.setTypeId(forumOfPost.getParent());

            String typeName = null;

            if(forumOfPost.getType().equals(ForumType.GROUP)){
                Optional<Group> findGroup = groupRepository.findByIdAndIsDeletedFalse(forumOfPost.getParent());

                if(findGroup.isEmpty()){
                    throw new ResourceNotFoundException("Group not found");
                }

                typeName = findGroup.get().getTitle();
            }

            else if(forumOfPost.getType().equals(ForumType.GAME)){
                Optional<Game> findGame = gameRepository.findByIdAndIsDeletedFalse(forumOfPost.getParent());

                if(findGame.isEmpty()){
                    throw new ResourceNotFoundException("Game not found");
                }

                typeName = findGame.get().getGameName();
            }

            Optional<User> findPoster = userRepository.findByIdAndIsDeletedFalse(post.getPoster());
            findPoster.ifPresent(dto::setPoster);

            Optional<Achievement> findAchievement = achievementRepository.findByIdAndIsDeletedFalse(post.getAchievement());
            findAchievement.ifPresent(dto::setAchievement);

            Optional<Character> findCharacter = characterRepository.findByIdAndIsDeletedFalse(post.getCharacter());
            findCharacter.ifPresent(dto::setCharacter);

            dto.setUserVote(getUserVote(post.getId(), user.getId()));

            dto.setTypeName(typeName);
            if(index<=promotedCount){
                dto.setIsPromoted(true);
            }else {
                dto.setIsPromoted(false);
            }
            index++;
            first20dto.add(dto);
        }

        return first20dto;
    }

    public VoteChoice getUserVote(String postId, String userId){
        Optional<Post> findPost = postRepository.findByIdAndIsDeletedFalse(postId);

        if(findPost.isEmpty()) return null;

        Optional<Vote> userVote = voteRepository.findByTypeIdAndVotedBy(postId,userId);

        return userVote.map(Vote::getChoice).orElse(null);
    }

    public List<Tag> populatedTags(List<String> tagIds){
        List<Tag> res = new ArrayList<>();

        for(String tagId : tagIds){
            Optional<Tag> findTag = tagRepository.findByIdAndIsDeletedFalse(tagId);
            findTag.ifPresent(res::add);
        }

        return res;
    }
}
