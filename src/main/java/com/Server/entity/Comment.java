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

    private User user;

    private Post post;

    private String imgUrl;

    private String content;

    @CreatedDate
    private Instant createdAt;

    @Override
    public String toString() {
        return "Comment{" +
                "_id='" + _id + '\'' +
                ", user=" + user +
                ", post=" + post +
                ", imgUrl='" + imgUrl + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
