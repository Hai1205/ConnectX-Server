package com.Server.entity;

import com.Server.dto.PostDTO;
import com.Server.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
@JsonIgnoreProperties({"password"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "_id"
)
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    public User(String username, String fullName, String email, String profileImgUrl) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.profileImgUrl = profileImgUrl;
    }

    public User(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    @Id
    private String _id;

    @NotBlank(message = "Username is required")
    private String username;

    private String fullName;

    @Email(message = "Email is invalid")
    private String email;

    @Size(min = 6, message = "Password must at least 6 characters")
    private String  password;

    @DBRef
    private List<User> followerList = new ArrayList<>();

    @DBRef
    private List<User> followingList = new ArrayList<>();

    @DBRef
    private List<Post> bookmarkedPostList = new ArrayList<>();

    private String profileImgUrl = "https://connect-x-server.s3.ap-southeast-1.amazonaws.com/avatar-placeholder.png";

    private String coverImgUrl = "https://connect-x-server.s3.ap-southeast-1.amazonaws.com/cover.png";

    private String bio;

    private String link;

    @DBRef
    private List<Post> likedPostList = new ArrayList<>();

    @DBRef
    private List<Post> postList = new ArrayList<>();

    @DBRef
    private List<Post> sharedPostList = new ArrayList<>();

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
                ", fullName='" + fullName + '\'' +
                ", followerList=" + followerList +
                ", followingList=" + followingList +
                ", profileImgUrl='" + profileImgUrl + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", bio='" + bio + '\'' +
                ", link='" + link + '\'' +
                ", likedList='" + likedPostList + '\'' +
                ", postList='" + postList + '\'' +
                ", sharedList='" + sharedPostList + '\'' +
                ", bookmarkedPostList='" + bookmarkedPostList + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
