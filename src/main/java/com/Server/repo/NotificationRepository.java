package com.Server.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Server.entity.Notification;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    boolean deleteByFrom(String userId);

    List<Notification> findByTo(String userId);
}
