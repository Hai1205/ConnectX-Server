package com.Server.dto;

import com.Server.entity.Post;
import com.Server.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    public UserDTO() {
    }

    public UserDTO(String _id, String username, String fullName, String profileImgUrl) {
        this._id = _id;
        this.username = username;
        this.fullName = fullName;
        this.profileImgUrl = profileImgUrl;
    }

    private String _id;

    private String username;

    private String email;

    private String fullName;

    private String profileImgUrl;

    private String coverImgUrl;

    private String bio;

    private String link;

    private List<PostDTO> postList = new ArrayList<>();

    private List<UserDTO> followerList = new ArrayList<>();

    private List<UserDTO> followingList = new ArrayList<>();

    private List<PostDTO> likedPostList = new ArrayList<>();

    private List<PostDTO> sharedPostList = new ArrayList<>();

    private List<PostDTO> bookmarkedPostList = new ArrayList<>();

    private Instant createdAt;
}
