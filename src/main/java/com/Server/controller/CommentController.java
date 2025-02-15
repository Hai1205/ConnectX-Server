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

    @GetMapping("/all-comments")
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

    @GetMapping("/post-comments/{postId}")
    public ResponseEntity<Response> userComments(@PathVariable("postId") String postId) {
        Response response = commentsApi.postComments(postId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/create-comments")
    public ResponseEntity<Response> createComments(
            @RequestParam("formData") String formData,
            @RequestParam("img") MultipartFile img
    ) {
        Response response = commentsApi.createComments(formData, img);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-comments/{commentId}")
    public ResponseEntity<Response> updateComments(
            @PathVariable("commentId") String commentId,
            @RequestParam("formData") String formData,
            @RequestParam("img") MultipartFile img
    ) {
        Response response = commentsApi.updateComments(commentId, formData, img);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
