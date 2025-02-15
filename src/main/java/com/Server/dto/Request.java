package com.Server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Request {
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
