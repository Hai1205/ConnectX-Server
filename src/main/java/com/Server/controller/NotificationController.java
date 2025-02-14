package com.Server.controller;

import com.Server.dto.Response;
import com.Server.entity.Notification;
import com.Server.service.api.NotificationsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationsApi notificationsApi;

}
