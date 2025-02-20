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

    @GetMapping("/get-all-posts")
    public ResponseEntity<Response> getAllPosts() {
        Response response = postsApi.getAllPosts();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

        @PostMapping("/create/{userId}")
        public ResponseEntity<Response> createPost(
                @PathVariable("userId") String userId,
                @RequestParam(value = "photos", required = false) List<MultipartFile> photos,
                @RequestPart(value = "content", required = false) String content
        ) {
            Response response = postsApi.createPost(photos, content, userId);
    
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Response> deletePost(@PathVariable("postId") String postId) {
        Response response = postsApi.deletePost(postId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/like/{postId}/{userId}")
    public ResponseEntity<Response> likePost(
            @PathVariable("postId") String postId,
            @PathVariable("userId") String userId
    ) {
        Response response = postsApi.likePost(postId, userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/bookmark/{postId}/{userId}")
    public ResponseEntity<Response> bookmarkPost(
            @PathVariable("postId") String postId,
            @PathVariable("userId") String userId
    ) {
        Response response = postsApi.bookmarkPost(postId, userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/share/{postId}/{userId}")
    public ResponseEntity<Response> sharePost(
            @PathVariable("postId") String postId,
            @PathVariable("userId") String userId
    ) {
        Response response = postsApi.sharePost(postId, userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-liked-posts/{userId}")
    public ResponseEntity<Response> getLikedPosts(
            @PathVariable("userId") String userId
    ) {
        Response response = postsApi.getLikedPosts(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-bookmarked-posts/{userId}")
    public ResponseEntity<Response> getBookmarkedPosts(@PathVariable("userId") String userId) {
        Response response = postsApi.getBookmarkedPosts(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-following-posts/{userId}")
    public ResponseEntity<Response> getFollowingPosts(@PathVariable("userId") String userId) {
        Response response = postsApi.getFollowingPosts(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-user-posts/{userId}")
    public ResponseEntity<Response> getUserPosts(@PathVariable("userId") String userId) {
        Response response = postsApi.getUserPosts(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
