package com.Server.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String _id;

    @CreatedDate
    private Instant createdAt;

    @Override
    public String toString() {
        return "Post{" +
                "_id=" + _id + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}