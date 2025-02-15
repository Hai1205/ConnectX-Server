package com.Server.controller;

import com.Server.dto.Request;
import com.Server.dto.Response;
import com.Server.entity.User;
import com.Server.service.api.AuthApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthApi authApi;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = authApi.register(user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody Request request, HttpServletResponse response) {
        Response loginResponse = authApi.login(request);

        if (loginResponse.getStatusCode() == 200) {
            int SevenDays = 7 * 24 * 60 * 60;
            Cookie jwtCookie = new Cookie("JWT_TOKEN", loginResponse.getToken());
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(SevenDays);

            response.addCookie(jwtCookie);
        }

        return ResponseEntity.status(loginResponse.getStatusCode()).body(loginResponse);
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "Logout successful!";
    }

    @GetMapping("/me")
    public ResponseEntity<Response> me() {
        Response response = authApi.me();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}