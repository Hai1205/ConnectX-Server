package com.Server.controller;

import com.Server.dto.Response;
import com.Server.entity.User;
import com.Server.service.api.UsersApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UsersApi usersApi;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "asc") String order
    ) {
        Response response = usersApi.getAllUsers(page, limit, sort, order);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<Response> profile(@RequestBody String username) {
        Response response = usersApi.profile(username);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/suggested")
    public ResponseEntity<Response> suggested() {
        Response response = usersApi.suggested();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/follow/{currentUserId}")
    public ResponseEntity<Response> followOrUnfollow(@PathVariable("currentUserId") String currentUserId, @RequestParam String userToModifyId) {
        Response response = usersApi.followOrUnfollow(currentUserId, userToModifyId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<Response> updateUser(@RequestBody String userId) {
        Response response = usersApi.updateUser(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
