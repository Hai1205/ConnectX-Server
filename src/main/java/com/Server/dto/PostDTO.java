package com.Server.dto;

import com.Server.entity.Comment;
import com.Server.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    public PostDTO() {
    }

    public PostDTO(String _id) {
        this._id = _id;
    }

    private String _id;

    private UserDTO user;

    private String content;

    private List<String> imageUrlList = new ArrayList<>();

    private List<UserDTO> bookmarkList = new ArrayList<>();

    private List<UserDTO> shareList = new ArrayList<>();

    private List<UserDTO> likeList = new ArrayList<>();

    private List<CommentDTO> commentList = new ArrayList<>();

    private Instant createdAt;
}
