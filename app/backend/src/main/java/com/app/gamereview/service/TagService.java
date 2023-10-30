package com.app.gamereview.service;

import com.app.gamereview.dto.request.tag.CreateTagRequestDto;
import com.app.gamereview.dto.request.tag.GetAllTagsFilterRequestDto;
import com.app.gamereview.dto.request.tag.UpdateTagRequestDto;
import com.app.gamereview.model.Tag;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

	private final TagRepository tagRepository;

	private final MongoTemplate mongoTemplate;

	private final ModelMapper modelMapper;

	@Autowired
	public TagService(
			TagRepository tagRepository,
			MongoTemplate mongoTemplate,
			ModelMapper modelMapper
	) {
		this.tagRepository = tagRepository;
		this.mongoTemplate = mongoTemplate;
		this.modelMapper = modelMapper;
	}

	public List<Tag> getAllTags(GetAllTagsFilterRequestDto filter) {
		Query query = new Query();
		if (filter.getName() != null) {
			query.addCriteria(Criteria.where("name").is(filter.getName()));
		}
		if (filter.getColor() != null) {
			query.addCriteria(Criteria.where("color").is(filter.getColor()));
		}
		if (filter.getTagType() != null) {
			query.addCriteria(Criteria.where("tagType").is(filter.getTagType()));
		}
		if (filter.getIsDeleted() != null) {
			query.addCriteria(Criteria.where("isDeleted").is(filter.getIsDeleted()));
		}

		return mongoTemplate.find(query, Tag.class);
	}

	public Tag getTagById(String id){
		Optional<Tag> tag = tagRepository.findById(id);

		return tag.orElse(null);
	}

	public Tag createTag(CreateTagRequestDto request){

		Optional<Tag> sameName = tagRepository
				.findByNameAndIsDeletedFalse(request.getName());

		if (sameName.isPresent()) {
			// TODO : will add exception handling mechanism and custom exceptions
			return null;
		}
		Tag tagToCreate = modelMapper.map(request, Tag.class);
		tagToCreate.setIsDeleted(false);
		tagToCreate.setCreatedAt(LocalDateTime.now());
		return tagRepository.save(tagToCreate);
	}

	public Tag updateTag(String id, UpdateTagRequestDto request){
		Optional<Tag> tag = tagRepository.findById(id);

		if (tag.isEmpty() || tag.get().getIsDeleted()){
			// TODO : will add exception handling mechanism and custom exceptions
			return null;
		}

		Tag tagToUpdate = modelMapper.map(request, Tag.class);
		tagToUpdate.setId(id);
		tagToUpdate.setIsDeleted(false);
		tagToUpdate.setCreatedAt(LocalDateTime.now());
		return tagRepository.save(tagToUpdate);
	}
}