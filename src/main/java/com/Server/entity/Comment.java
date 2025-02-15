package com.Server.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "comments")
public class Comment {
    @Id
    private String _id;

    private String userId;

    private String postId;

    private String img;

    private String text;

    @CreatedDate
    private Instant createdAt;

    @Override
    public String toString() {
        return "Comment{" +
                "_id='" + _id + '\'' +
                ", userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", img='" + img + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
