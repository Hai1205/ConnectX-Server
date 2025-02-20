package com.Server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Request {
    //Auth
    //    @NotBlank(message = "Email is required")
    private String email;

    //    @NotBlank(message = "Username is required")
    private String username;

    //    @NotBlank(message = "Password is required")
    private String password;

    // Post
    private String postId;

    private String userId;

    private String text;

    // User
    private String fullName;

    private String newPassword;

    private String confirmPassword;

    private String currentPassword;

    private String bio;

    private String link;
}
