package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.LoginRequestDTO;
import com.github.beltraliny.testeuex.models.dtos.LoginResponseDTO;
import com.github.beltraliny.testeuex.models.dtos.UserDTO;
import com.github.beltraliny.testeuex.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

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
        return new LoginResponseDTO(user.getName(), token);
    }

    public LoginResponseDTO register(UserDTO userDTO) {
        Optional<User> user = this.userService.findByUsername(userDTO.username());
        if (user.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        User newUser = this.userService.create(userDTO);
        String token = this.tokenService.createToken(newUser);

        return new LoginResponseDTO(newUser.getName(), token);
    }
}
