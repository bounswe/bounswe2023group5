package com.app.gamereview.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.app.gamereview.dto.request.post.CreatePostRequestDto;
import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Post;
import com.app.gamereview.repository.PostRepository;

@Service
public class PostService {

  private final PostRepository postRepository;
  private final MongoTemplate mongoTemplate;
  private final ModelMapper modelMapper;

  @Autowired
  public PostService(PostRepository postRepository, MongoTemplate mongoTemplate, ModelMapper modelMapper) {
    this.postRepository = postRepository;
    this.mongoTemplate = mongoTemplate;
    this.modelMapper = modelMapper;
  }

  public List<GetPostListResponseDto> getPostList(GetPostListFilterRequestDto filter) {
    Query query = new Query();

    if (filter != null) {
      if (filter.getFindDeleted() == null || !filter.getFindDeleted()) {
        query.addCriteria(Criteria.where("isDeleted").is(false));
      }
      if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
        query.addCriteria(Criteria.where("title").regex(filter.getSearch(), "i"));
      }
    }

    List<Post> postList = mongoTemplate.find(query, Post.class);

    return postList.stream().map(this::mapToGetPostListResponseDto).collect(Collectors.toList());
  }

  private GetPostListResponseDto mapToGetPostListResponseDto(Post post) {
    Boolean isEdited = post.getCreatedAt().isBefore(post.getLastEditedAt());
    GetPostListResponseDto postDto = new GetPostListResponseDto(post.getId(), post.getTitle(), post.getPostContent(),
        post.getPoster(), post.getLastEditedAt(), post.getCreatedAt(), isEdited, post.getTags(),
        post.getInappropriate());
    return postDto;
  }

  public Post getPostById(String id) {
    Optional<Post> post = postRepository.findById(id);

    if (post.isEmpty()) {
      throw new ResourceNotFoundException("The post with the given id was not found");
    }

    return post.orElse(null);
  }

  public Post createPost(CreatePostRequestDto request) {
    Post postToCreate = modelMapper.map(request, Post.class);
    return postRepository.save(postToCreate);
  }
}
