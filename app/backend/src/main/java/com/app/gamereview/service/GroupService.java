package com.app.gamereview.service;

import com.app.gamereview.dto.request.group.CreateGroupRequestDto;
import com.app.gamereview.dto.request.group.GetAllGroupsFilterRequestDto;
import com.app.gamereview.dto.request.tag.AddGameTagRequestDto;
import com.app.gamereview.enums.*;
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

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final GameRepository gameRepository;

    private final ForumRepository forumRepository;

    private final TagRepository tagRepository;

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper;

    @Autowired
    public GroupService(
            GroupRepository groupRepository,
            GameRepository gameRepository,
            ForumRepository forumRepository,
            TagRepository tagRepository,
            MongoTemplate mongoTemplate,
            ModelMapper modelMapper
    ) {
        this.groupRepository = groupRepository;
        this.gameRepository = gameRepository;
        this.forumRepository = forumRepository;
        this.tagRepository = tagRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;

        modelMapper.addMappings(new PropertyMap<CreateGroupRequestDto, Group>() {
            @Override
            protected void configure() {
                map().setGameId(source.getGameId());
                skip().setId(null); // Exclude id from mapping
            }
        });
    }

    public List<Group> getAllGroups(GetAllGroupsFilterRequestDto filter){
        Query query = new Query();

        // search for title
        if(filter.getTitle() != null && !filter.getTitle().isBlank()){
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
            if(game.isEmpty()){
                return new ArrayList<>();
            }
            query.addCriteria(Criteria.where("gameId").is(game.get().getId()));
        }
        if (filter.getSortBy() != null) {
            Sort.Direction sortDirection = Sort.Direction.DESC;
            if (filter.getSortDirection() != null) {
                sortDirection = filter.getSortDirection().equals(SortDirection.ASCENDING.name()) ? Sort.Direction.ASC
                        : Sort.Direction.DESC;
            }
            if (filter.getSortBy().equals(SortType.CREATION_DATE.name())) {
                query.with(Sort.by(sortDirection, "createdAt"));
            }
            else if (filter.getSortBy().equals(SortType.QUOTA.name())) {
                query.with(Sort.by(sortDirection, "quota"));
            }
        }

        return mongoTemplate.find(query,Group.class);
    }

    public Group getGroupById(String groupId){
        Optional<Group> isGroupExists = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if(isGroupExists.isEmpty()){
            throw new ResourceNotFoundException("Group not found");
        }

        return isGroupExists.get();
    }

    public Group createGroup(CreateGroupRequestDto request, User user){

        Optional<Group> sameTitle = groupRepository.findByTitleAndIsDeletedFalse(request.getTitle());

        if(sameTitle.isPresent()){
            throw new BadRequestException("Group with same title already exists, please pick a new title");
        }

        if(request.getTags() != null){
            for(String tagId : request.getTags()){
                Optional<Tag> tag = tagRepository.findByIdAndIsDeletedFalse(tagId);
                if(tag.isEmpty()){
                    throw new ResourceNotFoundException("One of the added tag is not found");
                }
            }
        }

        if(request.getGameId() != null){
            Optional<Game> game = gameRepository.findByIdAndIsDeletedFalse(request.getGameId());
            if(game.isEmpty()){
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

    public Boolean joinGroup(String groupId, User user){
        Optional<Group> isGroupExists = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if(isGroupExists.isEmpty()){
            throw new ResourceNotFoundException("Group not found");
        }

        if(MembershipPolicy.PRIVATE.equals(isGroupExists.get().getMembershipPolicy())){
            throw new BadRequestException("You should send join request for this group");
        }

        if(isGroupExists.get().getQuota() <= isGroupExists.get().getMembers().size()){
            throw new BadRequestException("You cannot join, group is full");
        }

        isGroupExists.get().addMember(user.getId());
        groupRepository.save(isGroupExists.get());

        return true;
    }

    public Boolean leaveGroup(String groupId, User user){
        Optional<Group> isGroupExists = groupRepository.findByIdAndIsDeletedFalse(groupId);

        if(isGroupExists.isEmpty()){
            throw new ResourceNotFoundException("Group not found");
        }


        isGroupExists.get().removeMember(user.getId());
        groupRepository.save(isGroupExists.get());

        return true;
    }

}
