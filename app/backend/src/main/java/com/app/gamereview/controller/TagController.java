package com.app.gamereview.controller;

import com.app.gamereview.dto.request.tag.CreateTagRequestDto;
import com.app.gamereview.dto.request.tag.GetAllTagsFilterRequestDto;
import com.app.gamereview.dto.request.tag.UpdateTagRequestDto;
import com.app.gamereview.model.Tag;
import com.app.gamereview.service.TagService;
import com.app.gamereview.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@Validated
public class TagController {

	private final TagService tagService;

	@Autowired
	public TagController(
			TagService tagService
	) {
		this.tagService = tagService;
	}

	@GetMapping("/get-all")
	public ResponseEntity<List<Tag>> getTags(GetAllTagsFilterRequestDto filter) {
		List<Tag> tags = tagService.getAllTags(filter);
		return ResponseEntity.ok(tags);
	}

	@GetMapping("/get")
	public ResponseEntity<Tag> getTag(@RequestParam String id) {
		Tag tag = tagService.getTagById(id);

		return ResponseEntity.ok(tag);
	}

	@PostMapping("/create")
	public ResponseEntity<Tag> createTag(@Valid @RequestBody CreateTagRequestDto createTagRequestDto) {
		Tag tagToCreate = tagService.createTag(createTagRequestDto);
		return ResponseEntity.ok(tagToCreate);
	}

	@PutMapping("/update")
	public ResponseEntity<Tag> updateTag(
			@RequestParam String id,
			@Valid @RequestBody UpdateTagRequestDto updateTagRequestDto) {

		Tag updatedTag = tagService.updateTag(id, updateTagRequestDto);
		return ResponseEntity.ok(updatedTag);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Boolean> deleteTag(@RequestParam String id){
		return ResponseEntity.ok(tagService.deleteTag(id));
	}

}
