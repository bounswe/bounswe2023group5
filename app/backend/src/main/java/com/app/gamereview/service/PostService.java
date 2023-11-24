package com.app.gamereview.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.app.gamereview.dto.response.comment.CommentReplyResponseDto;
import com.app.gamereview.dto.response.comment.GetPostCommentsResponseDto;
import com.app.gamereview.dto.response.post.GetPostDetailResponseDto;
import com.app.gamereview.enums.SortDirection;
import com.app.gamereview.enums.SortType;
import com.app.gamereview.enums.UserRole;
import com.app.gamereview.enums.VoteChoice;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import org.modelmapper.ModelMapper;
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

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    private final VoteRepository voteRepository;

    private final AchievementRepository achievementRepository;

    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public PostService(PostRepository postRepository, ForumRepository forumRepository, UserRepository userRepository,
                       ProfileRepository profileRepository, TagRepository tagRepository,
                       CommentRepository commentRepository, VoteRepository voteRepository,
                       AchievementRepository achievementRepository, MongoTemplate mongoTemplate,
                       ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
        this.voteRepository = voteRepository;
        this.achievementRepository = achievementRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
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

        // Fetch tags individually
        for (String tagId : post.getTags()) {
            Optional<Tag> tag = tagRepository.findById(tagId);
            tag.ifPresent(tags::add);
        }

        return new GetPostListResponseDto(post.getId(), post.getTitle(), post.getPostContent(),
                posterObject, userVoteChoice, post.getLastEditedAt(), post.getCreatedAt(), isEdited, tags,
                post.getInappropriate(), post.getOverallVote(), post.getVoteCount(), commentCount);
    }


    public GetPostDetailResponseDto getPostById(String id, User user) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new ResourceNotFoundException("The post with the given id was not found");
        }
        Optional<Forum> forum = forumRepository.findById(post.get().getForum());

        if (forum.isPresent()) {
            List<String> bannedUsers = forum.get().getBannedUsers();
            if (bannedUsers.contains(user.getId())) {
                throw new ResourceNotFoundException("You cannot see the post because you are banned.");
            }
        }

        GetPostDetailResponseDto postDto = modelMapper.map(post, GetPostDetailResponseDto.class);

        Optional<Achievement> postAchievement = achievementRepository.findByIdAndIsDeletedFalse(post.get().getAchievement());

        postAchievement.ifPresent(postDto::setAchievement);

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

    public List<GetPostCommentsResponseDto> getCommentList(String postId, User user) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            throw new ResourceNotFoundException("The post with the given id was not found");
        }
        Optional<Forum> forum = forumRepository.findById(post.get().getForum());
        if (forum.isPresent()) {
            List<String> bannedUsers = forum.get().getBannedUsers();
            if (bannedUsers.contains(user.getId())) {
                throw new ResourceNotFoundException("You cannot see the comments because you are banned from this forum.");
            }
        }
        List<Comment> comments = commentRepository.findByPost(postId);

        Map<String, GetPostCommentsResponseDto> commentMap = new HashMap<>();
        List<GetPostCommentsResponseDto> topLevelComments = new ArrayList<>();

        // First, convert all comments to DTOs and identify top-level comments
        for (Comment comment : comments) {
            GetPostCommentsResponseDto dto = convertToCommentDto(comment);
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
                    parentDto.getReplies().add(convertToReplyDto(comment));
                }
            }
        }

        return topLevelComments;
    }

    private GetPostCommentsResponseDto convertToCommentDto(Comment comment) {
        Boolean isEdited = comment.getCreatedAt().isBefore(comment.getLastEditedAt());
        String commenterId = comment.getCommenter();
        Optional<User> commenter = userRepository.findByIdAndIsDeletedFalse(commenterId);

        User commenterObject = commenter.orElse(null);
        return new GetPostCommentsResponseDto(comment.getId(), comment.getCommentContent(), commenterObject,
                comment.getPost(), comment.getLastEditedAt(), comment.getCreatedAt(), isEdited, comment.getIsDeleted(), comment.getOverallVote(),
                comment.getVoteCount(), new ArrayList<>());
    }

    private CommentReplyResponseDto convertToReplyDto(Comment comment) {
        Boolean isEdited = comment.getCreatedAt().isBefore(comment.getLastEditedAt());
        String commenterId = comment.getCommenter();
        Optional<User> commenter = userRepository.findByIdAndIsDeletedFalse(commenterId);

        User commenterObject = commenter.orElse(null);
        return new CommentReplyResponseDto(comment.getId(), comment.getCommentContent(), commenterObject,
                comment.getPost(), comment.getLastEditedAt(), comment.getCreatedAt(), isEdited, comment.getIsDeleted(), comment.getOverallVote(),
                comment.getVoteCount());
    }


    public Post createPost(CreatePostRequestDto request, User user) {

        Optional<Forum> forum = forumRepository.findById(request.getForum());

        if (forum.isEmpty()) {
            throw new ResourceNotFoundException("Forum is not found.");
        }

        if(request.getAchievement() != null){   // if achievement is assigned
            Optional<Achievement> achievementOptional = achievementRepository.findById(request.getAchievement());

            if (achievementOptional.isEmpty()) {
                throw new ResourceNotFoundException("Achievement is not found.");
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
        }

        profile.setPostCount(profile.getPostCount() + 1);
        profileRepository.save(profile);

        Post postToCreate = modelMapper.map(request, Post.class);
        postToCreate.setPoster(user.getId());
        postToCreate.setLastEditedAt(postToCreate.getCreatedAt());
        postToCreate.setInappropriate(false);
        postToCreate.setLocked(false);
        return postRepository.save(postToCreate);
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
}
