package com.Server.controller;

import com.Server.dto.Response;
import com.Server.service.api.PostsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostsApi postsApi;

    @GetMapping("/all-posts")
    public ResponseEntity<Response> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Response response = postsApi.getAllPosts(page, limit, sort, order);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<Response> createPost(
            @PathVariable("userId") String userId,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
            @RequestPart(value = "text", required = false) String text
    ) {
        Response response = postsApi.createPost(photos, text, userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Response> deletePost(@PathVariable("postId") String postId) {
        Response response = postsApi.deletePost(postId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/like/{userId}")
    public ResponseEntity<Response> likePost(
            @PathVariable("userId") String userId,
            @RequestParam("postId") String postId
    ) {
        Response response = postsApi.likePost(userId, postId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/liked-posts/{userId}")
    public ResponseEntity<Response> likedPosts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @PathVariable("userId") String userId
    ) {
        Response response = postsApi.likedPosts(page, limit, sort, order, userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/following-posts/{userId}")
    public ResponseEntity<Response> followingPosts(@PathVariable("userId") String userId) {
        Response response = postsApi.followingPosts(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user-posts/{userId}")
    public ResponseEntity<Response> userPosts(@PathVariable("userId") String userId) {
        Response response = postsApi.userPosts(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
