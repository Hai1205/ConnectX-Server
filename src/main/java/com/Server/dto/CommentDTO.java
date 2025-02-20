package com.Server.dto;

import com.Server.entity.Post;
import com.Server.entity.User;
import lombok.Data;

import java.time.Instant;

@Data
public class CommentDTO {
    public CommentDTO(String _id) {
        this._id = _id;
    }

    public CommentDTO() {
    }

    private String _id;

    private UserDTO user;

    private PostDTO post;

    private String imgUrl;

    private String content;

    private Instant createdAt;
}
