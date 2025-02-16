package com.Server.controller;

import com.Server.dto.Response;
import com.Server.service.api.NotificationsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationsApi notificationsApi;

    @GetMapping("/all-notifications")
    public ResponseEntity<Response> getAllNotifications(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ) {
        Response response = notificationsApi.getAllNotifications(page, limit, sort, order);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<Response> deleteNotification(@PathVariable("notificationId") String notificationId) {
        Response response = notificationsApi.deleteNotification(notificationId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user-notifications/{userId}")
//    public ResponseEntity<Response> userNotifications(@PathVariable("userId") String userId) {
    public String userNotifications(@PathVariable("userId") String userId) {
        Response response = notificationsApi.userNotifications(userId);

        return "hello";
//        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
