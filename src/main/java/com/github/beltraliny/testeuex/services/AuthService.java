package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.LoginRequestDTO;
import com.github.beltraliny.testeuex.models.dtos.LoginResponseDTO;
import com.github.beltraliny.testeuex.models.dtos.UserRequestDTO;
import com.github.beltraliny.testeuex.security.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = this.userService.findByUsername(loginRequestDTO.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        boolean isPasswordMatch = this.passwordEncoder.matches(loginRequestDTO.password(), user.getPassword());
        if (!isPasswordMatch) return null;

        String token = tokenService.createToken(user);
        return new LoginResponseDTO(user.getName(), user.getId(), token);
    }

    public LoginResponseDTO register(UserRequestDTO userRequestDTO) {
        User newUser = this.userService.create(userRequestDTO);
        String token = this.tokenService.createToken(newUser);

        return new LoginResponseDTO(newUser.getName(), newUser.getId(), token);
    }
}
