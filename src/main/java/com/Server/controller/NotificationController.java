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

    @GetMapping("/get-all-notifications")
    public ResponseEntity<Response> getAllNotifications(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order
    ) {
        Response response = notificationsApi.getAllNotifications(page, limit, sort, order);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete-by-id/{notificationId}")
    public ResponseEntity<Response> deleteNotificationById(@PathVariable("notificationId") String notificationId) {
        Response response = notificationsApi.deleteNotificationById(notificationId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete-by-userid/{userId}")
    public ResponseEntity<Response> deleteNotificationByUserId(@PathVariable("userId") String userId) {
        Response response = notificationsApi.deleteNotificationByUserId(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get-user-notifications/{userId}")
    public ResponseEntity<Response> getUserNotifications(@PathVariable("userId") String userId) {
        Response response = notificationsApi.getUserNotifications(userId);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
