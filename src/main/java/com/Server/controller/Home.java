package com.Server.controller;

import org.springframework.web.bind.annotation.*;

import com.Server.service.config.MailConfig;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class Home {
    @Autowired
    private MailConfig mailConfig;

    @GetMapping
    public String home() {
        return "Connect X Server";
    }

    @PostMapping("/send-template-email")
    public String sendTemplateEmail(@RequestParam String to) {
        String subject = "Xác nhận đặt phòng thành công!";
        String templateName = "mail-confirm-password";
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", to);
        variables.put("orderAmount", "$500");
        variables.put("orderCode", UUID.randomUUID().toString());

        try {
            mailConfig.sendHtmlEmail(to, subject, templateName, variables);
            return "Email đã được gửi thành công!";
        } catch (MessagingException e) {
            return "Gửi email thất bại: " + e.getMessage();
        }
    }
}
