package com.Server.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Server.entity.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
