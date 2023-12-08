package com.app.gamereview.service;

import com.app.gamereview.dto.request.post.GetPostListFilterRequestDto;
import com.app.gamereview.dto.response.post.GetPostListResponseDto;
import com.app.gamereview.model.Notification;
import com.app.gamereview.model.Post;
import com.app.gamereview.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.app.gamereview.enums.SortDirection.DESCENDING;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, MongoTemplate mongoTemplate,
                               ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
    }

    public List<Notification> getNotificationList(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Notification> notificationList = mongoTemplate.find(query, Notification.class);
        return notificationList;

    }

}
