package com.Server.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String _id;

    @DBRef
    private User user;

    private String content;

    private List<String> imageUrlList = new ArrayList<>();

    @DBRef
    private List<User> bookmarkList = new ArrayList<>();

    @DBRef
    private List<User> shareList = new ArrayList<>();

    @DBRef
    private List<User> likeList = new ArrayList<>();

    @DBRef
    private List<Comment> commentList = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @Override
    public String toString() {
        return "Post{" +
                "_id='" + _id + '\'' +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", imageUrlList=" + imageUrlList +
                ", bookmarkList=" + bookmarkList +
                ", shareList=" + shareList +
                ", likeList=" + likeList +
                ", commentList=" + commentList +
                ", createdAt=" + createdAt +
                '}';
    }
}