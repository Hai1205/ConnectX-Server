package com.Server.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String _id;

    @NotBlank(message = "Username is required")
    private String username;

    private String fullName;

    @Email(message = "Email is invalid")
    private String email;

    @Size(min = 6, message = "Password must at least 6 characters")
    private String  password;

    private String  confirmPassword;

    private String currentPassword;

    private String newPassword;

    private List<String> followerList = new ArrayList<>();

    private List<String> followingList = new ArrayList<>();

    private String profileImg;

    private String coverImg;

    private String bio;

    private String link;

    private List<String> likedList = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("User"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", followers=" + followerList +
                ", following=" + followingList +
                ", profileImg='" + profileImg + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", bio='" + bio + '\'' +
                ", link='" + link + '\'' +
                ", liked='" + likedList + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
