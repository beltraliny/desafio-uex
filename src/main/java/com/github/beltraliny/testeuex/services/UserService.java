package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.UserDTO;
import com.github.beltraliny.testeuex.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID create(UserDTO userDTO) {
        User newUser = new User(userDTO);
        User savedUser = this.userRepository.save(newUser);
        return savedUser.getUserId();
    }

    public Optional<User> findById(String id) {
        return this.userRepository.findById(UUID.fromString(id));
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public void update(String id, UserDTO userDTO) {
        UUID userUUID = UUID.fromString(id);

        Optional<User> user = this.userRepository.findById(userUUID);
        if (user.isEmpty()) return;

        User userToBeUpdated = user.get();
        if (!userDTO.username().isEmpty()) userToBeUpdated.setUsername(userDTO.username());
        if (!userDTO.email().isEmpty()) userToBeUpdated.setEmail(userDTO.email());
        if (!userDTO.password().isEmpty()) userToBeUpdated.setPassword(userDTO.password());

        this.userRepository.save(userToBeUpdated);
    }

    public void delete(String id) {
        UUID userUUID = UUID.fromString(id);

        boolean userExists = this.userRepository.existsById(userUUID);
        if (!userExists) return;

        this.userRepository.deleteById(userUUID);
    }
}
