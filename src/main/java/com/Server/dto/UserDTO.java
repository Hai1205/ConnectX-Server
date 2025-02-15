package com.Server.dto;

import com.Server.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String _id;

    private String username;

    private String password;

    private String fullName;

    private String email;

    private List<String> followerList = new ArrayList<>();

    private List<String> followingList = new ArrayList<>();

    private String profileImg;

    private String coverImg;

    private String bio;

    private String link;

    private List<String> likedList = new ArrayList<>();

    private Instant createdAt;
}
