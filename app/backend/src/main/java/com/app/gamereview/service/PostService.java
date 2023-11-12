package com.app.gamereview.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.gamereview.enums.SortDirection;
import com.app.gamereview.enums.SortType;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.model.Forum;
import com.app.gamereview.model.Tag;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.ForumRepository;
import com.app.gamereview.repository.TagRepository;
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
import com.app.gamereview.model.Post;
import com.app.gamereview.repository.PostRepository;

@Service
public class PostService {

  private final PostRepository postRepository;

  private final ForumRepository forumRepository;

  private final TagRepository tagRepository;
  private final MongoTemplate mongoTemplate;
  private final ModelMapper modelMapper;

  @Autowired
  public PostService(PostRepository postRepository, ForumRepository forumRepository, TagRepository tagRepository, MongoTemplate mongoTemplate, ModelMapper modelMapper) {
    this.postRepository = postRepository;
    this.forumRepository = forumRepository;
    this.tagRepository = tagRepository;
    this.mongoTemplate = mongoTemplate;
    this.modelMapper = modelMapper;
  }

  public List<GetPostListResponseDto> getPostList(GetPostListFilterRequestDto filter) {
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

    return postList.stream().map(this::mapToGetPostListResponseDto).collect(Collectors.toList());
  }

  private GetPostListResponseDto mapToGetPostListResponseDto(Post post) {
    Boolean isEdited = post.getCreatedAt().isBefore(post.getLastEditedAt());

      return new GetPostListResponseDto(post.getId(), post.getTitle(), post.getPostContent(),
        post.getPoster(), post.getLastEditedAt(), post.getCreatedAt(), isEdited, post.getTags(),
        post.getInappropriate(), post.getOverallVote(), post.getVoteCount());
  }

  public Post getPostById(String id) {
    Optional<Post> post = postRepository.findById(id);

    if (post.isEmpty()) {
      throw new ResourceNotFoundException("The post with the given id was not found");
    }

    return post.orElse(null);
  }

  public Post createPost(CreatePostRequestDto request, User user) {

    Optional<Forum> forum = forumRepository.findById(request.getForum());

    if (forum.isEmpty()) {
      throw new ResourceNotFoundException("Forum is not found.");
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

    if (!post.get().getPoster().equals(user.getId())){
      throw new BadRequestException("Only the user that created the post can edit it.");
    }

    Post editedPost = post.get();
    if (request.getTitle() != null && !request.getTitle().isEmpty()) {
      editedPost.setTitle(request.getTitle());
    }

    if (request.getPostContent() != null && !request.getPostContent().isEmpty()) {
      editedPost.setPostContent(request.getPostContent());
    }

    if((request.getPostContent() != null && !request.getPostContent().isEmpty()) || (request.getTitle() != null && !request.getTitle().isEmpty())) {
      editedPost.setLastEditedAt(LocalDateTime.now());
    }

    return postRepository.save(editedPost);
  }

  public Post deletePost(String id, User user) {
    Optional<Post> post = postRepository.findById(id);

    if (post.isEmpty() || post.get().getIsDeleted()) {
      throw new ResourceNotFoundException("The post with the given id is not found.");
    }

    if (!post.get().getPoster().equals(user.getId())){
      throw new BadRequestException("Only the user that created the post can delete it.");
    }

    Post postToDelete = post.get();

    postToDelete.setIsDeleted(true);

    return postRepository.save(postToDelete);
  }
}
