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

    @GetMapping("/all-users")
    public ResponseEntity<Response> getAllUsers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ) {
        Response response = usersApi.getAllUsers(page, limit, sort, order);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<Response> profile(@PathVariable("userId") String userId) {
        Response response = usersApi.profile(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/suggested/{userId}")
    public ResponseEntity<Response> suggested(@PathVariable("userId") String userId) {
        Response response = usersApi.suggested(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/follow/{currentUserId}")
    public ResponseEntity<Response> followOrUnfollow(
            @PathVariable("currentUserId") String currentUserId,
            @RequestParam("userToModifyId") String userToModifyId
    ) {
        Response response = usersApi.followOrUnfollow(currentUserId, userToModifyId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Response> updateUser(
            @RequestParam(name = "profileImg", value = "profileImg", required = false) MultipartFile profileImg,
            @RequestParam(name = "coverImg", value = "coverImg", required = false) MultipartFile coverImg,
            @RequestPart(name = "formData", value = "formData", required = false) String formData,
            @PathVariable("userId") String userId
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
