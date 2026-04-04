package com.jobtracker.application.controller;

import com.jobtracker.application.model.request.LoginRequest;
import com.jobtracker.application.model.request.RegisterRequest;
import com.jobtracker.application.service.AuthService;
import com.jobtracker.application.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${end.points.auth}")
public class AuthController {

    final AuthService authService;

    @PostMapping("${end.points.login}")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) {
        log.trace("login request:{}", request);
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    //register
    @PostMapping("${end.points.register}")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        log.trace("register request:{}", request);
        authService.register(request);
        return ResponseEntity.ok("register success");
    }
}
