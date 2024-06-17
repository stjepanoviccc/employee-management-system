package com.example.ems_app.controller;

import com.example.ems_app.dto.LoginRequestDTO;
import com.example.ems_app.dto.LoginResponseDTO;
import com.example.ems_app.dto.UserDTO;
import com.example.ems_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
      return ResponseEntity.ok(authService.login(loginRequestDTO));
    };

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(CREATED).body(authService.register(userDTO));
    };
}
