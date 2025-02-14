package com.Server.controller;

import com.Server.dto.LoginRequest;
import com.Server.dto.Response;
import com.Server.entity.User;
import com.Server.service.api.UsersApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UsersApi usersApi;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = usersApi.register(user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Response loginResponse = usersApi.login(loginRequest);

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

    @PostMapping("/log")
    public ResponseEntity<Response> logout() {
        Response response = usersApi.logout();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}