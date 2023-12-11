package com.app.gamereview.service;

import com.app.gamereview.dto.request.notification.CreateNotificationRequestDto;
import com.app.gamereview.dto.request.notification.GetNotificationsRequestDto;
import com.app.gamereview.model.Notification;
import com.app.gamereview.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
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

    public List<Notification> getNotificationList(String userId, GetNotificationsRequestDto filter) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user").is(userId));
        if(filter != null && filter.getIsRead() != null){
            query.addCriteria(Criteria.where("isRead").is(filter.getIsRead()));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Notification> notificationList = mongoTemplate.find(query, Notification.class);

        // create a deep copy to return without isRead being changed
        List<Notification> copiedList = new ArrayList<>();
        for (Notification notification : notificationList) {
            copiedList.add(new Notification(notification));
        }

        if(filter == null || filter.getIsRead() == null){
            for (Notification notification : notificationList) {
                if(!notification.getIsRead()) {
                    notification.setIsRead(true);
                    notificationRepository.save(notification);
                }
            }
        }
        return copiedList;
    }

    public Notification createNotification(CreateNotificationRequestDto request){
        Notification notificationToCreate = modelMapper.map(request, Notification.class);
        notificationToCreate.setIsDeleted(false);
        notificationToCreate.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notificationToCreate);
    }

}
