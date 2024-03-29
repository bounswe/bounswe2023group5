package com.app.gamereview.service;

import com.app.gamereview.dto.request.group.*;
import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.response.group.GetGroupDetailResponseDto;
import com.app.gamereview.dto.response.group.GetGroupResponseDto;
import com.app.gamereview.dto.response.group.GroupApplicationResponseDto;
import com.app.gamereview.dto.response.tag.AddGroupTagResponseDto;
import com.app.gamereview.enums.*;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.*;
import com.app.gamereview.repository.*;
import com.app.gamereview.util.UtilExtensions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final GameRepository gameRepository;

    private final ForumRepository forumRepository;

    private final TagRepository tagRepository;

    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;
    private final GroupApplicationRepository groupApplicationRepository;

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper;
    private final GameService gameService;
    private final NotificationService notificationService;

    @Autowired
    public GroupService(
            GroupRepository groupRepository,
            GameRepository gameRepository,
            ForumRepository forumRepository,
            TagRepository tagRepository,
            UserRepository userRepository,
            ProfileRepository profileRepository,
            GroupApplicationRepository groupApplicationRepository,
            MongoTemplate mongoTemplate,
            ModelMapper modelMapper,
            GameService gameService,
            NotificationService notificationService
    ) {
        this.groupRepository = groupRepository;
        this.gameRepository = gameRepository;
        this.forumRepository = forumRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.groupApplicationRepository = groupApplicationRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
        this.gameService = gameService;
        this.notificationService = notificationService;

        modelMapper.addMappings(new PropertyMap<CreateGroupRequestDto, Group>() {
            @Override
            protected void configure() {
                map().setGameId(source.getGameId());
                skip().setId(null); // Exclude id from mapping
            }
        });

        modelMapper.addMappings(new PropertyMap<Group, GetGroupResponseDto>() {
            @Override
            protected void configure() {
                skip().setTags(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<Group, GetGroupDetailResponseDto>() {
            @Override
            protected void configure() {
                skip().setTags(null);
                skip().setModerators(null);
                skip().setMembers(null);
                skip().setBannedMembers(null);
            }
        });
    }

    public List<GetGroupResponseDto> getAllGroups(GetAllGroupsFilterRequestDto filter, String email) {

        Optional<User> loggedInUser = userRepository.findByEmailAndIsDeletedFalse(email);
        String loggedInUserId = loggedInUser.map(User::getId).orElse(null);

        Query query = new Query();

        // search for title
        if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
            String regexPattern = ".*" + filter.getTitle() + ".*";
            query.addCriteria(Criteria.where("title").regex(regexPattern, "i"));
        }
        if (filter.getMembershipPolicy() != null) {
            query.addCriteria(Criteria.where("membershipPolicy").is(filter.getMembershipPolicy()));
        }
        if (filter.getTags() != null && !filter.getTags().isEmpty()) {
            query.addCriteria(Criteria.where("tags").in(filter.getTags()));
        }
        if (filter.getGameName() != null && !filter.getGameName().isBlank()) {
            String gameName = filter.getGameName();
            Optional<Game> game = gameRepository.findByGameNameAndIsDeletedFalse(gameName);
            if (game.isEmpty()) {
                return new ArrayList<>();
            }
            query.addCriteria(Criteria.where("gameId").is(game.get().getId()));
        }
        if (!filter.getWithDeleted()) {
            query.addCriteria(Criteria.where("isDeleted").in(filter.getWithDeleted()));
        }
        if (filter.getSortBy() != null) {
            Sort.Direction sortDirection = Sort.Direction.DESC;
            if (filter.getSortDirection() != null) {
                sortDirection = filter.getSortDirection().equals(SortDirection.ASCENDING.name()) ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
            }
            if (filter.getSortBy().equals(SortType.CREATION_DATE.name())) {
                query.with(Sort.by(sortDirection, "createdAt"));
            } else if (filter.getSortBy().equals(SortType.QUOTA.name())) {
                query.with(Sort.by(sortDirection, "quota"));
            }
        }

        List<Group> filteredGroups = mongoTemplate.find(query, Group.class);
        List<GetGroupResponseDto> responseDtos = new ArrayList<>();

        for (Group group : filteredGroups) {
            GetGroupResponseDto dto = modelMapper.map(group, GetGroupResponseDto.class);

            for (String tagId : group.getTags()) {
                Optional<Tag> tag = tagRepository.findById(tagId);
                tag.ifPresent(dto::populateTag);
            }

            dto.setUserJoined(group.getMembers().contains(loggedInUserId));

            responseDtos.add(dto);
        }

        return responseDtos;
    }

    public GetGroupDetailResponseDto getGroupById(String groupId, String email) {

        Optional<User> loggedInUser = userRepository.findByEmailAndIsDeletedFalse(email);
        String loggedInUserId = loggedInUser.map(User::getId).orElse(null);

        Optional<Group> isGroupExists = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if (isGroupExists.isEmpty()) {
            throw new ResourceNotFoundException("Group not found");
        }

        Group group = isGroupExists.get();
        GetGroupDetailResponseDto dto = modelMapper.map(group, GetGroupDetailResponseDto.class);

        for (String tagId : group.getTags()) {
            Optional<Tag> tag = tagRepository.findById(tagId);
            tag.ifPresent(dto::populateTag);
        }

        dto.setMembers(mapMemberIdsToMemberInfos(group.getMembers()));
        dto.setModerators(mapMemberIdsToMemberInfos(group.getModerators()));
        dto.setBannedMembers(mapMemberIdsToMemberInfos(group.getBannedMembers()));

        dto.setUserJoined(group.getMembers().contains(loggedInUserId));

        return dto;
    }

    public List<GetGroupDetailResponseDto.MemberInfo> mapMemberIdsToMemberInfos(List<String> memberIds) {
        List<GetGroupDetailResponseDto.MemberInfo> memberInfos = new ArrayList<>();

        for (String memberId : memberIds) {

            Optional<Profile> profileOfMember = profileRepository.findByUserIdAndIsDeletedFalse(memberId);
            Optional<User> user = userRepository.findByIdAndIsDeletedFalse(memberId);

            if (user.isEmpty() || profileOfMember.isEmpty())
                continue;

            GetGroupDetailResponseDto.MemberInfo memberInfo = new GetGroupDetailResponseDto.MemberInfo();
            memberInfo.id = memberId;
            memberInfo.username = user.get().getUsername();
            memberInfo.photoUrl = profileOfMember.get().getProfilePhoto();

            memberInfos.add(memberInfo);
        }
        return memberInfos;
    }

    public Group createGroup(CreateGroupRequestDto request, User user) {

        Optional<Group> sameTitle = groupRepository.findByTitleAndIsDeletedFalse(request.getTitle());

        if (sameTitle.isPresent()) {
            throw new BadRequestException("Group with same title already exists, please pick a new title");
        }

        if (request.getTags() != null) {
            for (String tagId : request.getTags()) {
                Optional<Tag> tag = tagRepository.findByIdAndIsDeletedFalse(tagId);
                if (tag.isEmpty()) {
                    throw new ResourceNotFoundException("One of the added tag is not found");
                }
                if (!tag.get().getTagType().name().equals(TagType.GROUP.name())) {
                    throw new BadRequestException("Groups can only be tagged with group tags");
                }
            }
        }

        if (request.getGameId() != null) {
            Optional<Game> game = gameRepository.findByIdAndIsDeletedFalse(request.getGameId());
            if (game.isEmpty()) {
                throw new ResourceNotFoundException("Game is not found");
            }
        }

        Group groupToCreate = modelMapper.map(request, Group.class);

        Forum correspondingForum = new Forum(groupToCreate.getTitle(), ForumType.GROUP,
                groupToCreate.getId(), new ArrayList<>(), new ArrayList<>());
        forumRepository.save(correspondingForum);
        groupToCreate.setForumId(correspondingForum.getId());

        List<String> moderators = new ArrayList<>();
        moderators.add(user.getId());
        List<String> members = new ArrayList<>();
        members.add(user.getId());

        groupToCreate.setModerators(moderators);
        groupToCreate.setMembers(members);

        return groupRepository.save(groupToCreate);
    }

    public Boolean deleteGroup(String identifier) {
        Optional<Group> foundGroup;

        if (UtilExtensions.isUUID(identifier)) {
            foundGroup = groupRepository.findByIdAndIsDeletedFalse(identifier);
        } else {
            foundGroup = groupRepository.findByTitleAndIsDeletedFalse(identifier);
        }

        if (foundGroup.isEmpty()) {
            throw new ResourceNotFoundException("Group is not found");
        }

        Group groupToDelete = foundGroup.get();

        groupToDelete.setIsDeleted(true);
        groupRepository.save(groupToDelete);
        return true;
    }

    public Group updateGroup(String groupId, UpdateGroupRequestDto request) {
        Optional<Group> foundGroup = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if (foundGroup.isEmpty()) {
            throw new ResourceNotFoundException("Group does not exist");
        }

        Group groupToUpdate = foundGroup.get();

        if (groupToUpdate.getMembers().size() > request.getQuota()) {
            throw new BadRequestException("Quota cannot be less than the current number of members in group");
        }

        // could also use a mapper (few values to assign hence this implementation is kind of handy)
        groupToUpdate.setTitle(request.getTitle());
        groupToUpdate.setDescription(request.getDescription());
        groupToUpdate.setMembershipPolicy(MembershipPolicy.valueOf(request.getMembershipPolicy()));
        groupToUpdate.setQuota(request.getQuota());
        groupToUpdate.setAvatarOnly(request.getAvatarOnly());
        groupToUpdate.setGroupIcon(request.getGroupIcon());
        groupRepository.save(groupToUpdate);
        return groupToUpdate;
    }

    public AddGroupTagResponseDto addGroupTag(AddGroupTagRequestDto request) {
        Optional<Group> findGroup = groupRepository.findById(request.getGroupId());

        Optional<Tag> findTag = tagRepository.findById(request.getTagId());

        if (findGroup.isEmpty() || findGroup.get().getIsDeleted()) {
            throw new ResourceNotFoundException("Group does not exist");
        }

        if (findTag.isEmpty() || findTag.get().getIsDeleted()) {
            throw new ResourceNotFoundException("Tag does not exist");
        }

        if (!findTag.get().getTagType().name().equals(TagType.GROUP.name())) {
            throw new BadRequestException("Only GROUP tags can be added to groups");
        }

        Group group = findGroup.get();
        Tag tag = findTag.get();

        if (group.getTags().contains(tag.getId())) {
            throw new BadRequestException("Tag is already added");
        }

        group.addTag(tag);
        groupRepository.save(group);

        AddGroupTagResponseDto response = new AddGroupTagResponseDto();
        response.setGroupId(group.getId());
        response.setAddedTag(tag);
        return response;
    }

    public Boolean removeGroupTag(RemoveGroupTagRequestDto request) {
        Optional<Group> findGroup = groupRepository.findById(request.getGroupId());

        Optional<Tag> findTag = tagRepository.findById(request.getTagId());

        if (findGroup.isEmpty() || findGroup.get().getIsDeleted()) {
            throw new ResourceNotFoundException("Group does not exist");
        }

        if (findTag.isEmpty() || findTag.get().getIsDeleted()) {
            throw new ResourceNotFoundException("Tag does not exist");
        }

        Group group = findGroup.get();
        Tag tag = findTag.get();

        if (!group.getTags().contains(tag.getId())) {
            return false;
        }

        group.removeTag(tag);
        groupRepository.save(group);

        return true;
    }

    public Boolean joinGroup(String groupId, User user) {
        Optional<Group> isGroupExists = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if (isGroupExists.isEmpty()) {
            throw new ResourceNotFoundException("Group not found");
        }

        if (MembershipPolicy.PRIVATE.equals(isGroupExists.get().getMembershipPolicy())) {
            throw new BadRequestException("You should send join request for this group");
        }

        if (isGroupExists.get().getQuota() <= isGroupExists.get().getMembers().size()) {
            throw new BadRequestException("You cannot join, group is full");
        }

        isGroupExists.get().addMember(user.getId());
        groupRepository.save(isGroupExists.get());

        return true;
    }

    public Boolean leaveGroup(String groupId, User user) {
        Optional<Group> isGroupExists = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if (isGroupExists.isEmpty()) {
            throw new ResourceNotFoundException("Group not found");
        }


        isGroupExists.get().removeMember(user.getId());
        groupRepository.save(isGroupExists.get());

        return true;
    }

    public Boolean banUser(String groupId, String userId, User user) {
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty() || group.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The group with the given id is not found.");
        }

        if (group.get().getModerators().contains(user.getId()) && group.get().getModerators().contains(userId)) {
            throw new BadRequestException("Moderators cannot ban moderators.");
        }

        if (!(group.get().getModerators().contains(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the moderator or the admin can ban user.");
        }

        group.get().addBannedUser(userId);
        groupRepository.save(group.get());

        Optional<User> optionalBannedUser = userRepository.findByIdAndIsDeletedFalse(userId);
        if (optionalBannedUser.isPresent()) {
            User bannedUser = optionalBannedUser.get();
            CreateNotificationRequestDto createNotificationRequestDto = new CreateNotificationRequestDto();
            String message = NotificationMessage.BANNED_FROM_GROUP.getMessageTemplate()
                    .replace("{user_name}", bannedUser.getUsername())
                    .replace("{group_title}", group.get().getTitle());
            createNotificationRequestDto.setMessage(message);
            createNotificationRequestDto.setParentType(NotificationParent.GROUP);
            createNotificationRequestDto.setParent(group.get().getId());
            createNotificationRequestDto.setUser(bannedUser.getId());
            notificationService.createNotification(createNotificationRequestDto);
        }

        return true;
    }

    public Boolean addModerator(String groupId, String userId, User user) {
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty() || group.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The group with the given id is not found.");
        }

        if (!(group.get().getModerators().contains(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the moderator or the admin can add moderator.");
        }

        group.get().addModerator(userId);
        groupRepository.save(group.get());

        return true;
    }

    public Boolean removeModerator(String groupId, String userId) {
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty() || group.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The group with the given id is not found.");
        }

        group.get().removeModerator(userId);
        groupRepository.save(group.get());

        return true;
    }

    public Boolean unbanUser(String groupId, String userId, User user) {
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty() || group.get().getIsDeleted()) {
            throw new ResourceNotFoundException("The group with the given id is not found.");
        }

        if (!(group.get().getModerators().contains(user.getId()) || UserRole.ADMIN.equals(user.getRole()))) {
            throw new BadRequestException("Only the moderator or the admin can unban user.");
        }

        group.get().removeBannedUser(userId);
        groupRepository.save(group.get());

        return true;
    }

    public Boolean applyGroup(String groupId, User user, GroupApplicationRequestDto dto) {
        Optional<Group> isGroupExists = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if (isGroupExists.isEmpty()) {
            throw new ResourceNotFoundException("Group not found");
        }
        Group group = isGroupExists.get();

        if (MembershipPolicy.PUBLIC.equals(group.getMembershipPolicy())) {
            throw new BadRequestException("This group is public, you can't apply to this group");
        }

        if (group.getMembers().contains(user.getId())) {
            throw new BadRequestException("You are already a member of this group so you can't apply.");
        }

        if (group.getBannedMembers().contains(user.getId())) {
            throw new BadRequestException("You are banned from this group so you can't apply.");
        }

        Optional<GroupApplication> groupJoinRequest = groupApplicationRepository.findByUserAndGroupAndStatus(user.getId(), groupId, GroupApplicationStatus.PENDING);

        if (groupJoinRequest.isPresent()) {
            throw new BadRequestException("You have a pending request for this group, please wait for the response.");
        }

        GroupApplication request = new GroupApplication();
        request.setUser(user.getId());
        request.setGroup(groupId);
        request.setMessage(dto.getMessage());
        request.setStatus(GroupApplicationStatus.PENDING);
        groupApplicationRepository.save(request);

        // send notification to moderators
        List<String> moderators = group.getModerators();
        for (String moderatorId : moderators) {
            Optional<User> optionalModerator = userRepository.findByIdAndIsDeletedFalse(moderatorId);
            if (optionalModerator.isPresent()) {
                User moderator = optionalModerator.get();
                CreateNotificationRequestDto createNotificationRequestDto = new CreateNotificationRequestDto();
                String message = NotificationMessage.NEW_GROUP_APPLICATION.getMessageTemplate()
                        .replace("{user_name}", user.getUsername())
                        .replace("{group_title}", group.getTitle());
                createNotificationRequestDto.setMessage(message);
                createNotificationRequestDto.setParentType(NotificationParent.GROUP_APPLICATION);
                createNotificationRequestDto.setParent(group.getId());
                createNotificationRequestDto.setUser(moderator.getId());
                notificationService.createNotification(createNotificationRequestDto);
            }
        }
        return true;
    }

    public Boolean reviewApplication(String applicationId, User user, GroupApplicationReviewDto dto) {
        Optional<GroupApplication> optionalApplication = groupApplicationRepository.findById(applicationId);

        if (optionalApplication.isEmpty()) {
            throw new ResourceNotFoundException("Application not found");
        }
        GroupApplication application = optionalApplication.get();

        if (!application.getStatus().equals(GroupApplicationStatus.PENDING)) {
            throw new BadRequestException("Application is already reviewed");
        }

        Optional<Group> optionalGroup = groupRepository.findByIdAndIsDeletedFalse(application.getGroup());
        if (optionalGroup.isEmpty()) {
            throw new ResourceNotFoundException("Applied group is not found");
        }
        Group appliedGroup = optionalGroup.get();

        if (!appliedGroup.getModerators().contains(user.getId())) {
            throw new BadRequestException("You are not a moderator of this group");
        }

        if (GroupApplicationReviewResult.APPROVE.name().equals(dto.getResult())) {
            if (appliedGroup.getMembers().size() >= appliedGroup.getQuota()) {
                throw new BadRequestException("Group is full");
            }
            if (appliedGroup.getBannedMembers().contains(application.getUser())) {
                throw new BadRequestException("User is banned from this group");
            }
            if (!appliedGroup.getMembers().contains(application.getUser())) {
                appliedGroup.addMember(application.getUser());
                groupRepository.save(appliedGroup);
            }
            application.setStatus(GroupApplicationStatus.APPROVED);
            application.setReviewer(user.getId());
            application.setReviewedAt(LocalDateTime.now());
            groupApplicationRepository.save(application);

            // send APPLICATION_ACCEPTED notification to user
            Optional<User> optionalUser = userRepository.findByIdAndIsDeletedFalse(application.getUser());
            if (optionalUser.isPresent()) {
                User appliedUser = optionalUser.get();
                CreateNotificationRequestDto createNotificationRequestDto = new CreateNotificationRequestDto();
                String message = NotificationMessage.APPLICATION_ACCEPTED.getMessageTemplate()
                        .replace("{user_name}", appliedUser.getUsername())
                        .replace("{group_title}", appliedGroup.getTitle());
                createNotificationRequestDto.setMessage(message);
                createNotificationRequestDto.setParentType(NotificationParent.GROUP);
                createNotificationRequestDto.setParent(application.getGroup());
                createNotificationRequestDto.setUser(appliedUser.getId());
                notificationService.createNotification(createNotificationRequestDto);
            }

        } else if (GroupApplicationReviewResult.REJECT.name().equals(dto.getResult())) {
            application.setStatus(GroupApplicationStatus.REJECTED);
            application.setReviewer(user.getId());
            application.setReviewedAt(LocalDateTime.now());
            groupApplicationRepository.save(application);

            // send APPLICATION_REJECTED notification to user
            Optional<User> optionalUser = userRepository.findByIdAndIsDeletedFalse(application.getUser());
            if (optionalUser.isPresent()) {
                User appliedUser = optionalUser.get();
                CreateNotificationRequestDto createNotificationRequestDto = new CreateNotificationRequestDto();
                String message = NotificationMessage.APPLICATION_REJECTED.getMessageTemplate()
                        .replace("{user_name}", appliedUser.getUsername())
                        .replace("{group_title}", appliedGroup.getTitle());
                createNotificationRequestDto.setMessage(message);
                createNotificationRequestDto.setParentType(NotificationParent.GROUP);
                createNotificationRequestDto.setParent(application.getGroup());
                createNotificationRequestDto.setUser(appliedUser.getId());
                notificationService.createNotification(createNotificationRequestDto);
            }
        } else {
            throw new BadRequestException("Invalid review result");
        }
        return true;
    }

    public List<GroupApplicationResponseDto> listApplications(String groupId, User user) {
        Optional<Group> isGroupExists = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if (isGroupExists.isEmpty()) {
            throw new ResourceNotFoundException("Group not found");
        }
        Group group = isGroupExists.get();

        if (!group.getModerators().contains(user.getId())) {
            throw new BadRequestException("You are not a moderator of this group");
        }

        List<GroupApplication> applications = groupApplicationRepository.findByGroupAndStatus(groupId, GroupApplicationStatus.PENDING);
        // for each application, convert to dto and append in a list
        List<GroupApplicationResponseDto> responseDtos = new ArrayList<>();
        for (GroupApplication application : applications) {
            responseDtos.add(contertToDto(application));
        }
        return responseDtos;
    }

    private GroupApplicationResponseDto contertToDto(GroupApplication application) {
        GroupApplicationResponseDto dto = new GroupApplicationResponseDto();
        dto.setId(application.getId());
        dto.setAppliedAt(application.getCreatedAt());

        userRepository.findById(application.getUser()).ifPresent(dto::setApplicant);
        groupRepository.findById(application.getGroup()).ifPresent(dto::setGroup);

        dto.setMessage(application.getMessage());
        dto.setStatus(application.getStatus());
        return dto;
    }

    public List<Group> getRecommendedGroups(String email) {
        Optional<User> optUser = userRepository.findByEmailAndIsDeletedFalse(email);

        if(optUser.isEmpty()){
            Query query = new Query();
            query.addCriteria(Criteria.where("isDeleted").is(false));
            query.limit(10);
            return mongoTemplate.find(query, Group.class);
        }
        User user = optUser.get();

        Optional<Profile> findProfile = profileRepository.findByUserIdAndIsDeletedFalse(user.getId());
        if (findProfile.isEmpty()) {
            throw new ResourceNotFoundException("Profile of the user is not found, unexpected error has occurred");
        }
        List<Group> recommendations = new ArrayList<>();
        List<Group> memberGroups = groupRepository.findUserGroups(user.getId());
        List<String> userGames = findProfile.get().getGames();
        if (memberGroups.isEmpty()) {
            if (userGames.isEmpty()){
                Query query = new Query();
                query.addCriteria(Criteria.where("isDeleted").is(false));
                query.limit(10);
                return mongoTemplate.find(query, Group.class);
            }else{
                List<Group> recommendationGameGroups = new ArrayList<>();
                for(String gameId : userGames){
                    Query query = new Query();
                    query.addCriteria(Criteria.where("isDeleted").is(false));
                    query.addCriteria(Criteria.where("gameId").is(gameId));
                    query.limit(2);
                    List<Group> gameGroups = mongoTemplate.find(query, Group.class);
                    recommendationGameGroups.addAll(gameGroups);
                    Collections.shuffle(recommendationGameGroups);
                }
                return recommendationGameGroups.subList(0, Math.min(10, recommendationGameGroups.size()));
            }
        }
        TreeSet<RecommendGroupDto> recommendedGroups = new TreeSet<>(Comparator.reverseOrder());
        for (Group group : memberGroups) {
            String groupId = group.getId();
            recommendedGroups.addAll(recommendationByGroupId(groupId, user.getId()));
        }
        if(!userGames.isEmpty()){
            List<Group> recommendedGameGroups = new ArrayList<>();
            for(String gameId : userGames){
                Query query = new Query();
                query.addCriteria(Criteria.where("isDeleted").is(false));
                query.addCriteria(Criteria.where("gameId").is(gameId));
                query.addCriteria(Criteria.where("members").nin(user.getId()));
                query.limit(2);
                List<Group> gameGroups = mongoTemplate.find(query, Group.class);
                recommendedGameGroups.addAll(gameGroups);
            }
            recommendations.addAll(recommendedGameGroups);
        }
        for (RecommendGroupDto groupDto : recommendedGroups) {
            recommendations.add(groupDto.getGroup());
        }

        return recommendations.subList(0, Math.min(10, recommendations.size()));
    }

    public TreeSet<RecommendGroupDto> recommendationByGroupId(String groupId, String userId) {
        Optional<Group> findGroup = groupRepository.findByIdAndIsDeletedFalse(groupId);

        Set<String> idList = new HashSet<>();

        if (findGroup.isEmpty()) {
            throw new ResourceNotFoundException("Group is not found");
        }

        Group group = findGroup.get();
        idList.add(group.getId());

        TreeSet<RecommendGroupDto> scoreSet = new TreeSet<>(Comparator.reverseOrder());

        String[] words = group.getTitle().split(" ", -2);
        for (String word : words) {
            if (word.length() > 3) {
                String regexPattern = ".*" + word + ".*";
                Query query = new Query();
                query.addCriteria(Criteria.where("title").regex(regexPattern, "i"));
                query.addCriteria(Criteria.where("_id").ne(group.getId()));
                query.addCriteria(Criteria.where("members").nin(userId));
                List<Group> similarNames = mongoTemplate.find(query, Group.class);
                for (Group i : similarNames) {
                    RecommendGroupDto dto = new RecommendGroupDto();
                    dto.setGroup(i);
                    scoreSet.add(dto);
                    idList.add(dto.getGroup().getId());
                }
            }
        }

        Query allGroupsQuery = new Query();
        allGroupsQuery.addCriteria(Criteria.where("isDeleted").is(false));
        allGroupsQuery.addCriteria(Criteria.where("_id").nin(idList));
        allGroupsQuery.addCriteria(Criteria.where("members").nin(userId));

        List<Group> allGroups = mongoTemplate.find(allGroupsQuery, Group.class);

        for (Group candidateGroup : allGroups) {
            int score = calculateGroupSimilarityScore(group, candidateGroup);
            if (score != 0) {
                RecommendGroupDto dto = new RecommendGroupDto();
                dto.setGroup(candidateGroup);
                dto.setScore(score);
                scoreSet.add(dto);
            }
        }

        return scoreSet;
    }

    public int calculateGroupSimilarityScore(Group based, Group candidate) {
        int score = 0;
        String gameId = based.getGameId();
        String candidateGameId = candidate.getGameId();


        if (gameId.equals(candidateGameId)) {
            score += 10;
        }
        Game basedGame = gameRepository.findById(gameId).get();
        Game candidateGame = gameRepository.findById(candidateGameId).get();

        int gameSimScore = gameService.calculateSimilarityScore(basedGame, candidateGame);
        score += gameSimScore;

        List<String> baseTags = based.getTags();
        baseTags.retainAll(candidate.getTags());

        for (String tagId : baseTags) {
            Optional<Tag> findTag = tagRepository.findByIdAndIsDeletedFalse(tagId);
            if (findTag.isPresent()) {
                score++;
            }
        }
        return score;
    }


}
