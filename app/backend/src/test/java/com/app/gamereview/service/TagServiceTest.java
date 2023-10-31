package com.app.gamereview.service;
import com.app.gamereview.dto.request.tag.CreateTagRequestDto;
import com.app.gamereview.dto.request.tag.GetAllTagsFilterRequestDto;
import com.app.gamereview.dto.request.tag.UpdateTagRequestDto;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.Tag;
import com.app.gamereview.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTags() {
        GetAllTagsFilterRequestDto filter = new GetAllTagsFilterRequestDto();
        Tag tag = new Tag();
        when(mongoTemplate.find(Mockito.any(), Mockito.eq(Tag.class))).thenReturn(Collections.singletonList(tag));

        List<Tag> result = tagService.getAllTags(filter);

        assertEquals(1, result.size());
        assertEquals(tag, result.get(0));
    }

    @Test
    void testGetTagById() {
        String tagId = "tagId";
        Tag tag = new Tag();
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        Tag result = tagService.getTagById(tagId);

        assertNotNull(result);
        assertEquals(tag, result);
    }

    @Test
    void testCreateTag() {
        CreateTagRequestDto request = new CreateTagRequestDto();
        request.setName("New Tag");

        Tag expectedTag = new Tag();
        expectedTag.setName("New Tag");

        when(tagRepository.findByNameAndIsDeletedFalse(request.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(request, Tag.class)).thenReturn(expectedTag);
        when(tagRepository.save(expectedTag)).thenReturn(expectedTag);

        Tag result = tagService.createTag(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertFalse(result.getIsDeleted());
        assertNotNull(result.getCreatedAt());

        verify(tagRepository).save(expectedTag);
    }


    @Test
    void testCreateTagWithExistingName() {
        CreateTagRequestDto request = new CreateTagRequestDto();
        request.setName("Existing Tag");

        when(tagRepository.findByNameAndIsDeletedFalse(request.getName())).thenReturn(Optional.of(new Tag()));

        assertThrows(BadRequestException.class, () -> tagService.createTag(request));
    }

    @Test
    void testUpdateTag() {
        String tagId = "tagId";
        UpdateTagRequestDto request = new UpdateTagRequestDto();
        Tag existingTag = new Tag();
        existingTag.setId(tagId);
        existingTag.setIsDeleted(false);
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(existingTag));
        when(modelMapper.map(request, Tag.class)).thenReturn(existingTag);
        when(tagRepository.save(existingTag)).thenReturn(existingTag);

        Tag result = tagService.updateTag(tagId, request);

        assertNotNull(result);
        assertEquals(tagId, result.getId());
        assertFalse(result.getIsDeleted());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void testUpdateTagNotFound() {
        String tagId = "nonExistentTagId";
        UpdateTagRequestDto request = new UpdateTagRequestDto();
        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tagService.updateTag(tagId, request));
    }
}
