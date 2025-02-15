package com.Server.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "notifications")
public class Notification {
    public Notification(String type, String from, String to) {
        this.type = type;
        this.from = from;
        this.to = to;
    }

    @Id
    private String _id;

    private String from;

    private String to;

    private String type;

    private boolean read;

    @CreatedDate
    private Instant createdAt;

    @Override
    public String toString() {
        return "Notification{" +
                "_id='" + _id + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", type='" + type + '\'' +
                ", read=" + read +
                ", createdAt=" + createdAt +
                '}';
    }
}
