package com.Server.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentDTO {
    public CommentDTO(String text, String userId) {
        this.text = text;
        this.userId = userId;
    }

    public CommentDTO() {
    }

    private String _id;

    private String userId;

    private String postId;

    private String img;

    private String text;

    private Instant createdAt;
}
