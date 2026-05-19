package com.example.controller;

import com.example.domain.User;
import com.example.service.UserService;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpStatus;
import java.util.Map;

@Controller("/v1/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Post("/register")
    @Status(HttpStatus.CREATED)
    public User register(@Body User user) {
        return userService.register(user);
    }

    @Post(value = "/token", consumes = io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED)
    public Map<String, Object> token(@Body Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        return userService.findByLogin(username)
                .filter(user -> user.getPassword().equals(password))
                .map(user -> Map.<String, Object>of(
                        "access_token", "fake-token-" + user.getId(),
                        "token_type", "Bearer",
                        "expires_in", 3600
                ))
                .orElseThrow(() -> new io.micronaut.http.exceptions.HttpStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    }
}