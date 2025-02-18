package com.Server.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String _id;

    private String userId;

    private String text;

    private List<String> imageList = new ArrayList<>();

    private List<String> likeList = new ArrayList<>();

    private List<String> commentList = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @Override
    public String toString() {
        return "Post{" +
                "_id='" + _id + '\'' +
                ", userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", imageList='" + imageList + '\'' +
                ", likes=" + likeList +
                ", comments=" + commentList +
                ", createdAt=" + createdAt +
                '}';
    }
}