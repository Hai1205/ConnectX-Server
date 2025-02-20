package com.Server.controller;

import com.Server.dto.Response;
import com.Server.service.api.CommentsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentsApi commentsApi;

    @GetMapping("/get-all-comments")
    public ResponseEntity<Response> getAllComments(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ) {
        Response response = commentsApi.getAllComments(page, limit, sort, order);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete-comment/{commentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable("commentId") String commentId) {
        Response response = commentsApi.deleteComment(commentId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete-post-comments/{postId}")
    public ResponseEntity<Response> deletePostComment(@PathVariable("postId") String postId) {
        Response response = commentsApi.deletePostComment(postId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-post-comments/{postId}")
    public ResponseEntity<Response> getUserComments(@PathVariable("postId") String postId) {
        Response response = commentsApi.getUserComments(postId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/create-comment/{postId}/{userId}")
    public ResponseEntity<Response> createComments(
            @PathVariable("postId") String postId,
            @PathVariable("userId") String userId,
            @RequestParam("text") String text,
            @RequestParam(value = "img", required = false) MultipartFile img
    ) {
        Response response = commentsApi.createComments(postId, userId, text, img);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-comment/{postId}/{commentId}")
    public ResponseEntity<Response> updateComments(
            @PathVariable("postId") String postId,
            @PathVariable("commentId") String commentId,
            @RequestParam("text") String text,
            @RequestParam("img") MultipartFile img
    ) {
        Response response = commentsApi.updateComments(postId, commentId, text, img);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
