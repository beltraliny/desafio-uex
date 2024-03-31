package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.UserDTO;
import com.github.beltraliny.testeuex.models.dtos.UserResponseDTO;
import com.github.beltraliny.testeuex.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserDTO userDTO) {
        User newUser = new User(userDTO);
        newUser.setPassword(this.passwordEncoder.encode(userDTO.password()));

        return this.userRepository.save(newUser);
    }

    public UserResponseDTO findById(String id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UserResponseDTO(user.getName(), user.getUsername());
    }

    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public List<UserResponseDTO> list() {
        List<User> userList = this.userRepository.findAll();
        List<UserResponseDTO> userResponseDTOList= userList
                .stream()
                .map(user -> new UserResponseDTO(user.getName(), user.getUsername()))
                .toList();
        return userResponseDTOList;
    }

    public void update(String id, UserDTO userDTO) {
        User userToBeUpdated = this.userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userDTO.name() != null) userToBeUpdated.setName(userDTO.name());
        if (userDTO.username() != null) userToBeUpdated.setUsername(userDTO.username());
        if (userDTO.password() != null) userToBeUpdated.setPassword(userDTO.password());

        this.userRepository.save(userToBeUpdated);
    }

    public void delete(String id) {
        boolean userExists = this.userRepository.existsById(id);
        if (!userExists) return;

        this.userRepository.deleteById(id);
    }
}
