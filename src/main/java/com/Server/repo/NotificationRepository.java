package com.Server.repo;

import com.Server.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.Server.entity.Notification;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByFrom(String userId);
}
