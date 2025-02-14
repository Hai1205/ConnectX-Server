package com.Server.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String _id;

    @CreatedDate
    private Instant createdAt;

    @Override
    public String toString() {
        return "Notification{" +
                "_id='" + _id + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
