package com.Server.controller;

import com.Server.dto.Response;
import com.Server.service.api.UsersApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UsersApi usersApi;

    @GetMapping("/get-all-users")
    public ResponseEntity<Response> getAllUsers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ) {
        Response response = usersApi.getAllUsers(page, limit, sort, order);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-profile/{username}")
    public ResponseEntity<Response> getProfile(@PathVariable("username") String username) {
        Response response = usersApi.getProfile(username);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-user-suggested/{userId}")
    public ResponseEntity<Response> getUserSuggested(@PathVariable("userId") String userId) {
        Response response = usersApi.getUserSuggested(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/follow/{currentUserId}/{userToModifyId}")
    public ResponseEntity<Response> followOrUnfollow(
            @PathVariable("currentUserId") String currentUserId,
            @PathVariable("userToModifyId") String userToModifyId
    ) {
        Response response = usersApi.followOrUnfollow(currentUserId, userToModifyId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Response> updateUser(
            @PathVariable("userId") String userId,
            @RequestParam(value = "formData", required = false) String formData,
            @RequestParam(value = "profileImg", required = false) MultipartFile profileImg,
            @RequestParam(value = "coverImg", required = false) MultipartFile coverImg
    ) {
        Response response = usersApi.updateUser(formData, profileImg, coverImg, userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId) {
        Response response = usersApi.deleteUser(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
