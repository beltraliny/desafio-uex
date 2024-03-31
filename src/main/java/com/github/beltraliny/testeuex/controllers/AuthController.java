package com.github.beltraliny.testeuex.controllers;

import com.github.beltraliny.testeuex.models.dtos.LoginRequestDTO;
import com.github.beltraliny.testeuex.models.dtos.LoginResponseDTO;
import com.github.beltraliny.testeuex.models.dtos.UserRequestDTO;
import com.github.beltraliny.testeuex.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        if (loginResponseDTO == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody UserRequestDTO userRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.register(userRequestDTO);
        if (loginResponseDTO == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(loginResponseDTO);
    }
}
