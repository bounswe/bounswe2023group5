package com.app.gamereview.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.app.gamereview.dto.request.group.*;
import com.app.gamereview.dto.response.group.GetGroupDetailResponseDto;
import com.app.gamereview.dto.response.group.GetGroupResponseDto;
import com.app.gamereview.dto.response.group.GroupApplicationResponseDto;
import com.app.gamereview.dto.response.tag.AddGroupTagResponseDto;
import com.app.gamereview.enums.*;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import com.app.gamereview.service.GroupService;
import com.app.gamereview.util.UtilExtensions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;

class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ForumRepository forumRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private GroupApplicationRepository groupApplicationRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGroup_Success() {
        // Arrange
        CreateGroupRequestDto requestDto = new CreateGroupRequestDto();
        requestDto.setTitle("Test Group");
        requestDto.setTags(Arrays.asList("tag1", "tag2"));
        requestDto.setGameId("gameId");

        User user = new User();
        user.setId("userId");

        Group groupToCreate = new Group();
        groupToCreate.setTitle("Test Group");
        //groupToCreate.setTags(Arrays.asList("tag1", "tag2"));
        groupToCreate.setGameId("gameId");

        //Forum correspondingForum = new Forum();

        Tag tag1 = new Tag();
        tag1.setTagType(TagType.GROUP);
        tag1.setId("tag1");

        Tag tag2 = new Tag();
        tag2.setTagType(TagType.GROUP);
        tag2.setId("tag2");
        Game game = new Game("Game 1", "Description 1", null, "SystemReq 1");
        game.setId("gameId");

        Forum forum = new Forum();
        forum.setId("forumId");
        forum.setName("ForumName");
        forum.setType(ForumType.GROUP);
        forum.setParent("ParentGroup");
        // Mock repository calls
        when(groupRepository.findByTitleAndIsDeletedFalse("Test Group")).thenReturn(Optional.empty());
        when(tagRepository.findByIdAndIsDeletedFalse("tag1")).thenReturn(Optional.of(tag1));
        when(tagRepository.findByIdAndIsDeletedFalse("tag2")).thenReturn(Optional.of(tag2));
        when(gameRepository.findByIdAndIsDeletedFalse("gameId")).thenReturn(Optional.of(game));
        when(modelMapper.map(any(), any())).thenReturn(groupToCreate);
        when(groupRepository.save(any())).thenReturn(groupToCreate);
        when(forumRepository.save(any())).thenReturn(forum);

        // Act
        Group createdGroup = groupService.createGroup(requestDto, user);

        // Assert
        assertNotNull(createdGroup);
        assertEquals("Test Group", createdGroup.getTitle());
        assertEquals("userId", createdGroup.getModerators().get(0));
        assertEquals("userId", createdGroup.getMembers().get(0));
    }

    @Test
    void createGroup_GroupWithTitleExists_ThrowsBadRequestException() {
        // Arrange
        CreateGroupRequestDto requestDto = new CreateGroupRequestDto();
        requestDto.setTitle("Test Group");

        User user = new User();

        // Mock repository calls
        when(groupRepository.findByTitleAndIsDeletedFalse("Test Group")).thenReturn(Optional.of(new Group()));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.createGroup(requestDto, user));
    }

    @Test
    void createGroup_TagNotFound_ThrowsResourceNotFoundException() {
        // Arrange
        CreateGroupRequestDto requestDto = new CreateGroupRequestDto();
        requestDto.setTitle("Test Group");
        requestDto.setTags(Collections.singletonList("tag1"));

        User user = new User();

        // Mock repository calls
        when(groupRepository.findByTitleAndIsDeletedFalse("Test Group")).thenReturn(Optional.empty());
        when(tagRepository.findByIdAndIsDeletedFalse("tag1")).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> groupService.createGroup(requestDto, user));
    }
    @Test
    void testGetAllGroups() {
        // Arrange
        GetAllGroupsFilterRequestDto filter = new GetAllGroupsFilterRequestDto();
        filter.setTitle("Test Group");
        filter.setSortBy(SortType.CREATION_DATE.name());
        filter.setSortDirection(SortDirection.DESCENDING.name());
        filter.setWithDeleted(false);

        String email = "user@example.com";
        User loggedInUser = new User();
        loggedInUser.setId("1");
        loggedInUser.setEmail(email);

        when(userRepository.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.of(loggedInUser));

        Group group = new Group();
        group.setId("1");
        group.setTitle("Test Group");
        group.setMembers(Arrays.asList("1", "2"));

        when(mongoTemplate.find(any(), eq(Group.class))).thenReturn(Arrays.asList(group));

        GetGroupResponseDto responseDto = new GetGroupResponseDto();
        responseDto.setId("1");
        responseDto.setTitle("Test Group");
        responseDto.setUserJoined(true);

        when(modelMapper.map(group, GetGroupResponseDto.class)).thenReturn(responseDto);

        // Act
        List<GetGroupResponseDto> result = groupService.getAllGroups(filter, email);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        GetGroupResponseDto resultDto = result.get(0);
        assertEquals("1", resultDto.getId());
        assertEquals("Test Group", resultDto.getTitle());
        assertTrue(resultDto.getUserJoined());
    }

    // Add more test cases as needed based on different scenarios and edge cases.
    @Test
    void testGetGroupById() {
        // Arrange
        String groupId = "1";
        String email = "user@example.com";

        User loggedInUser = new User();
        loggedInUser.setId("2");
        loggedInUser.setEmail(email);

        Profile profile = new Profile();
        profile.setUserId(loggedInUser.getId());

        when(userRepository.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.of(loggedInUser));

        Group group = new Group();
        group.setId(groupId);
        group.setTitle("Test Group");
        group.setMembers(Arrays.asList("1", "2"));
        group.setModerators(Arrays.asList("1"));
        group.setTags(Arrays.asList("tag1"));

        when(userRepository.findByIdAndIsDeletedFalse(loggedInUser.getId())).thenReturn(Optional.of(loggedInUser));
        when(profileRepository.findByUserIdAndIsDeletedFalse(loggedInUser.getId())).thenReturn(Optional.of(profile));


        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        GetGroupDetailResponseDto responseDto = new GetGroupDetailResponseDto();
        responseDto.setId(groupId);
        responseDto.setTitle("Test Group");
        responseDto.setUserJoined(true);


        Tag tag = new Tag();
        tag.setId("tag1");
        tag.setName("Test Tag");


        when(tagRepository.findById("tag1")).thenReturn(Optional.of(tag));
        when(modelMapper.map(group, GetGroupDetailResponseDto.class)).thenReturn(responseDto);


        // Act
        GetGroupDetailResponseDto result = groupService.getGroupById(groupId, email);
        System.out.println(result.getTags());
        // Assert
        assertNotNull(result);
        assertEquals(groupId, result.getId());
        assertEquals("Test Group", result.getTitle());
        assertTrue(result.getUserJoined());
        assertNotNull(result.getTags());
        assertEquals(1, result.getTags().size());
        assertEquals("tag1", result.getTags().get(0).getId());
        assertEquals("Test Tag", result.getTags().get(0).getName());
        //assertNotNull(result.getMembers());
        //assertEquals(2, result.getMembers().size());
        //assertNotNull(result.getModerators());
        //assertEquals(1, result.getModerators().size());
    }

    @Test
    void testGetGroupByIdNotFound() {
        // Arrange
        String groupId = "1";
        String email = "user@example.com";

        when(userRepository.findByEmailAndIsDeletedFalse(email)).thenReturn(Optional.of(new User()));
        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> groupService.getGroupById(groupId, email));
    }
    @Test
    void testDeleteGroupByIdentifier() {
        // Arrange
        String groupId = "1";

        Group group = new Group();
        group.setId(groupId);
        group.setTitle("Test Group");
        group.setIsDeleted(false);

        when(groupRepository.findByTitleAndIsDeletedFalse(group.getTitle())).thenReturn(Optional.of(group));

        // Act
        boolean result = groupService.deleteGroup(group.getTitle());

        // Assert
        assertTrue(result);
        assertTrue(group.getIsDeleted());
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testDeleteGroupByTitle() {
        // Arrange
        String groupTitle = "Test Group";

        Group group = new Group();
        group.setId("1");
        group.setTitle(groupTitle);
        group.setIsDeleted(false);

        when(groupRepository.findByTitleAndIsDeletedFalse(groupTitle)).thenReturn(Optional.of(group));

        // Act
        boolean result = groupService.deleteGroup(groupTitle);

        // Assert
        assertTrue(result);
        assertTrue(group.getIsDeleted());
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testDeleteGroupNotFound() {
        // Arrange
        String identifier = "nonexistent";

        when(groupRepository.findByIdAndIsDeletedFalse(identifier)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> groupService.deleteGroup(identifier));
    }

    @Test
    void testUpdateGroup() {
        // Arrange
        String groupId = "1";
        UpdateGroupRequestDto request = new UpdateGroupRequestDto();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setMembershipPolicy(MembershipPolicy.PUBLIC.name());
        request.setQuota(100);
        request.setAvatarOnly(true);

        Group group = new Group();
        group.setId(groupId);
        group.setTitle("Original Title");
        group.setDescription("Original Description");
        group.setMembershipPolicy(MembershipPolicy.PRIVATE);
        group.setQuota(50);
        group.setAvatarOnly(false);

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act
        Group updatedGroup = groupService.updateGroup(groupId, request);

        // Assert
        assertNotNull(updatedGroup);
        assertEquals(groupId, updatedGroup.getId());
        assertEquals("Updated Title", updatedGroup.getTitle());
        assertEquals("Updated Description", updatedGroup.getDescription());
        assertEquals(MembershipPolicy.PUBLIC, updatedGroup.getMembershipPolicy());
        assertEquals(100, updatedGroup.getQuota());
        assertTrue(updatedGroup.getAvatarOnly());
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testUpdateGroupNotFound() {
        // Arrange
        String groupId = "nonexistent";
        UpdateGroupRequestDto request = new UpdateGroupRequestDto();

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> groupService.updateGroup(groupId, request));
    }

    @Test
    void testUpdateGroupInvalidQuota() {
        // Arrange
        String groupId = "1";
        UpdateGroupRequestDto request = new UpdateGroupRequestDto();
        request.setQuota(1); // Assuming current members count is 50

        Group group = new Group();
        group.setId(groupId);
        group.setMembers(List.of("member1", "member2"));

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.updateGroup(groupId, request));
    }

    @Test
    void testJoinGroup() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);
        group.setMembershipPolicy(MembershipPolicy.PUBLIC);
        group.setQuota(5);
        group.setMembers(new ArrayList<>(List.of("user1")));

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act
        boolean result = groupService.joinGroup(groupId, user);

        // Assert
        assertTrue(result);
        assertTrue(group.getMembers().contains("user1"));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testJoinPrivateGroup() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);
        group.setMembershipPolicy(MembershipPolicy.PRIVATE);

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.joinGroup(groupId, user));
    }

    @Test
    void testJoinFullGroup() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);
        group.setMembershipPolicy(MembershipPolicy.PUBLIC);
        group.setQuota(2);
        group.setMembers(List.of("user2", "user3"));

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.joinGroup(groupId, user));
    }

    @Test
    void testLeaveGroup() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);
        group.setMembers(new ArrayList<>(List.of("user1", "user2", "user3")));

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act
        boolean result = groupService.leaveGroup(groupId, user);

        // Assert
        assertTrue(result);
        assertFalse(group.getMembers().contains("user1"));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testLeaveGroupNotFound() {
        // Arrange
        String groupId = "nonexistent";
        User user = new User();
        user.setId("user1");

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> groupService.leaveGroup(groupId, user));
    }

    @Test
    void testApplyGroup() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);
        group.setMembershipPolicy(MembershipPolicy.PRIVATE);
        group.setMembers(List.of("user2", "user3"));
        group.setBannedMembers(List.of("user4"));

        GroupApplicationRequestDto requestDto = new GroupApplicationRequestDto();
        requestDto.setMessage("Application message");

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));
        when(groupApplicationRepository.findByUserAndGroupAndStatus("user1", groupId, GroupApplicationStatus.PENDING))
                .thenReturn(Optional.empty());

        // Act
        boolean result = groupService.applyGroup(groupId, user, requestDto);

        // Assert
        assertTrue(result);
        verify(groupApplicationRepository, times(1)).save(any(GroupApplication.class));
    }

    @Test
    void testApplyGroupNotFound() {
        // Arrange
        String groupId = "nonexistent";
        User user = new User();
        user.setId("user1");
        GroupApplicationRequestDto requestDto = new GroupApplicationRequestDto();

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> groupService.applyGroup(groupId, user, requestDto));
    }

    @Test
    void testApplyGroupPublicGroup() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);
        group.setMembershipPolicy(MembershipPolicy.PUBLIC);

        GroupApplicationRequestDto requestDto = new GroupApplicationRequestDto();

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.applyGroup(groupId, user, requestDto));
    }

    @Test
    void testApplyGroupAlreadyMember() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);
        group.setMembers(List.of("user1"));

        GroupApplicationRequestDto requestDto = new GroupApplicationRequestDto();

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.applyGroup(groupId, user, requestDto));
    }

    @Test
    void testApplyGroupBannedMember() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);
        group.setBannedMembers(List.of("user1"));

        GroupApplicationRequestDto requestDto = new GroupApplicationRequestDto();

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.applyGroup(groupId, user, requestDto));
    }

    @Test
    void testApplyGroupPendingRequest() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("user1");

        Group group = new Group();
        group.setId(groupId);

        GroupApplicationRequestDto requestDto = new GroupApplicationRequestDto();

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));
        when(groupApplicationRepository.findByUserAndGroupAndStatus("user1", groupId, GroupApplicationStatus.PENDING))
                .thenReturn(Optional.of(new GroupApplication()));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.applyGroup(groupId, user, requestDto));
    }

    @Test
    void testReviewApplicationApprove() {
        // Arrange
        String applicationId = "1";
        User user = new User();
        user.setId("moderator1");


        GroupApplicationReviewDto reviewDto = new GroupApplicationReviewDto();
        reviewDto.setResult(GroupApplicationReviewResult.APPROVE.name());

        GroupApplication application = new GroupApplication();
        application.setId(applicationId);
        application.setStatus(GroupApplicationStatus.PENDING);
        application.setUser("user1");
        application.setGroup("groupId");

        Group appliedGroup = new Group();
        appliedGroup.setId("groupId");
        appliedGroup.setMembers(new ArrayList<>(List.of("user2", "user3")));
        appliedGroup.setQuota(5);
        appliedGroup.setModerators(new ArrayList<>(List.of("moderator1")));
        appliedGroup.setBannedMembers(new ArrayList<>(List.of("user4")));

        when(groupApplicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(groupRepository.findByIdAndIsDeletedFalse("groupId")).thenReturn(Optional.of(appliedGroup));

        // Act
        boolean result = groupService.reviewApplication(applicationId, user, reviewDto);

        // Assert
        assertTrue(result);
        assertEquals(GroupApplicationStatus.APPROVED, application.getStatus());
        assertEquals("moderator1", application.getReviewer());
        assertNotNull(application.getReviewedAt());
        assertTrue(appliedGroup.getMembers().contains("user1"));
        verify(groupRepository, times(1)).save(appliedGroup);
        verify(groupApplicationRepository, times(1)).save(application);
    }

    @Test
    void testReviewApplicationReject() {
        // Arrange
        String applicationId = "1";
        User user = new User();
        user.setId("moderator1");

        GroupApplicationReviewDto reviewDto = new GroupApplicationReviewDto();
        reviewDto.setResult(GroupApplicationReviewResult.REJECT.name());

        GroupApplication application = new GroupApplication();
        application.setId(applicationId);
        application.setStatus(GroupApplicationStatus.PENDING);
        application.setUser("user1");
        application.setGroup("groupId");

        Group appliedGroup = new Group();
        appliedGroup.setId("groupId");
        appliedGroup.setMembers(new ArrayList<>(List.of("user2", "user3")));
        appliedGroup.setQuota(5);
        appliedGroup.setModerators(new ArrayList<>(List.of("moderator1")));
        appliedGroup.setBannedMembers(new ArrayList<>(List.of("user4")));

        when(groupApplicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(groupRepository.findByIdAndIsDeletedFalse("groupId")).thenReturn(Optional.of(appliedGroup));


        // Act
        boolean result = groupService.reviewApplication(applicationId, user, reviewDto);

        // Assert
        assertTrue(result);
        assertEquals(GroupApplicationStatus.REJECTED, application.getStatus());
        assertEquals("moderator1", application.getReviewer());
        assertNotNull(application.getReviewedAt());
        verify(groupApplicationRepository, times(1)).save(application);
    }

    @Test
    void testReviewApplicationNotModerator() {
        // Arrange
        String applicationId = "1";
        User user = new User();
        user.setId("nonModerator");

        GroupApplicationReviewDto reviewDto = new GroupApplicationReviewDto();
        reviewDto.setResult(GroupApplicationReviewResult.APPROVE.name());

        GroupApplication application = new GroupApplication();
        application.setId(applicationId);
        application.setStatus(GroupApplicationStatus.PENDING);
        application.setGroup("groupId");

        Group appliedGroup = new Group();
        appliedGroup.setId("groupId");
        appliedGroup.setModerators(List.of("moderator1"));

        when(groupApplicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(groupRepository.findByIdAndIsDeletedFalse("groupId")).thenReturn(Optional.of(appliedGroup));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.reviewApplication(applicationId, user, reviewDto));
    }

    @Test
    void testListApplications() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("moderator1");

        Group group = new Group();
        group.setId(groupId);
        group.setModerators(List.of("moderator1"));

        GroupApplication application1 = new GroupApplication();
        application1.setId("app1");
        application1.setUser("user1");
        application1.setGroup(groupId);
        application1.setStatus(GroupApplicationStatus.PENDING);

        GroupApplication application2 = new GroupApplication();
        application2.setId("app2");
        application2.setUser("user2");
        application2.setGroup(groupId);
        application2.setStatus(GroupApplicationStatus.PENDING);

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));
        when(groupApplicationRepository.findByGroupAndStatus(groupId, GroupApplicationStatus.PENDING))
                .thenReturn(List.of(application1, application2));

        // Act
        List<GroupApplicationResponseDto> responseDtos = groupService.listApplications(groupId, user);

        // Assert
        assertNotNull(responseDtos);
        assertEquals(2, responseDtos.size());
        assertEquals("app1", responseDtos.get(0).getId());
        assertEquals(GroupApplicationStatus.PENDING, responseDtos.get(0).getStatus());
        assertEquals("app2", responseDtos.get(1).getId());
        assertEquals(GroupApplicationStatus.PENDING, responseDtos.get(1).getStatus());
    }

    @Test
    void testListApplicationsGroupNotFound() {
        // Arrange
        String groupId = "nonexistent";
        User user = new User();
        user.setId("moderator1");

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> groupService.listApplications(groupId, user));
        verify(groupApplicationRepository, never()).findByGroupAndStatus(anyString(), any());
    }

    @Test
    void testListApplicationsNotModerator() {
        // Arrange
        String groupId = "1";
        User user = new User();
        user.setId("nonModerator");

        Group group = new Group();
        group.setId(groupId);
        group.setModerators(List.of("moderator1"));

        when(groupRepository.findByIdAndIsDeletedFalse(groupId)).thenReturn(Optional.of(group));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.listApplications(groupId, user));
        verify(groupApplicationRepository, never()).findByGroupAndStatus(anyString(), any());
    }

    @Test
    void testAddGroupTag() {
        // Arrange
        AddGroupTagRequestDto requestDto = new AddGroupTagRequestDto();
        requestDto.setGroupId("groupId");
        requestDto.setTagId("tagId");

        Group group = new Group();
        group.setId("groupId");

        Tag tag = new Tag();
        tag.setId("tagId");
        tag.setTagType(TagType.GROUP);

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));
        when(tagRepository.findById("tagId")).thenReturn(Optional.of(tag));

        // Act
        AddGroupTagResponseDto responseDto = groupService.addGroupTag(requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals("groupId", responseDto.getGroupId());
        assertEquals(tag, responseDto.getAddedTag());
        assertTrue(group.getTags().contains("tagId"));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testAddGroupTagTagNotGroupType() {
        // Arrange
        AddGroupTagRequestDto requestDto = new AddGroupTagRequestDto();
        requestDto.setGroupId("groupId");
        requestDto.setTagId("tagId");

        Group group = new Group();
        group.setId("groupId");

        Tag tag = new Tag();
        tag.setId("tagId");
        tag.setTagType(TagType.PLATFORM);

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));
        when(tagRepository.findById("tagId")).thenReturn(Optional.of(tag));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.addGroupTag(requestDto));
        verify(groupRepository, never()).save(any());
    }

    @Test
    void testAddGroupTagTagAlreadyAdded() {
        // Arrange
        AddGroupTagRequestDto requestDto = new AddGroupTagRequestDto();
        requestDto.setGroupId("groupId");
        requestDto.setTagId("tagId");
        Tag grpTag = new Tag();
        grpTag.setId("tagId");
        Group group = new Group();
        group.setId("groupId");
        group.addTag(grpTag);

        Tag tag = new Tag();
        tag.setId("tagId");
        tag.setTagType(TagType.GROUP);

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));
        when(tagRepository.findById("tagId")).thenReturn(Optional.of(tag));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.addGroupTag(requestDto));
        verify(groupRepository, never()).save(any());
    }
    @Test
    void testRemoveGroupTag() {
        // Arrange
        RemoveGroupTagRequestDto requestDto = new RemoveGroupTagRequestDto();
        requestDto.setGroupId("groupId");
        requestDto.setTagId("tagId");

        Group group = new Group();
        group.setId("groupId");
        Tag grpTag = new Tag();
        grpTag.setId("tagId");
        group.addTag(grpTag);

        Tag tag = new Tag();
        tag.setId("tagId");

        when(groupRepository.findById("groupId")).thenReturn(Optional.of(group));
        when(tagRepository.findById("tagId")).thenReturn(Optional.of(tag));

        // Act
        boolean result = groupService.removeGroupTag(requestDto);

        // Assert
        assertTrue(result);
        assertFalse(group.getTags().contains("tagId"));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testAddModerator() {
        // Arrange
        String groupId = "groupId";
        String userId = "userId";

        Group group = new Group();
        group.setId(groupId);
        group.addModerator("moderatorId");

        User user = new User();
        user.setId("moderatorId");

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        // Act
        boolean result = groupService.addModerator(groupId, userId, user);

        // Assert
        assertTrue(result);
        assertTrue(group.getModerators().contains(userId));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testAddModeratorNotAllowed() {
        // Arrange
        String groupId = "groupId";
        String userId = "userId";

        Group group = new Group();
        group.setId(groupId);
        group.addModerator("otherModeratorId");

        User user = new User();
        user.setId("nonModerator");

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> groupService.addModerator(groupId, userId, user));
        verify(groupRepository, never()).save(any());
    }
    @Test
    void testRemoveModeratorNotFound() {
        String groupId = "groupId";
        String userId = "userId";
        String notGroupId = "groupId2";

        Group group = new Group();
        group.setId(groupId);
        group.addModerator(userId);

        User user = new User();
        user.setId(userId);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        assertThrows(ResourceNotFoundException.class, () -> groupService.removeModerator(notGroupId, userId));
        verify(groupRepository, never()).save(any());
    }

    @Test
    void testRemoveModerator() {
        // Arrange
        String groupId = "groupId";
        String moderatorId = "moderatorId";

        Group group = new Group();
        group.setId(groupId);
        group.addModerator(moderatorId);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        boolean result = groupService.removeModerator(groupId, moderatorId);

        assertTrue(result);
        assertFalse(group.getModerators().contains(moderatorId));
        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void testReviewApplicationNotFound() {
        String applicationId = "nonexistent";
        User user = new User();
        user.setId("moderator1");

        GroupApplicationReviewDto reviewDto = new GroupApplicationReviewDto();
        reviewDto.setResult(GroupApplicationReviewResult.APPROVE.name());

        when(groupApplicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> groupService.reviewApplication(applicationId, user, reviewDto));
        verify(groupRepository, never()).save(any());
    }



}

