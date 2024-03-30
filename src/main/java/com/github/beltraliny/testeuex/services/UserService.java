package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.UserDTO;
import com.github.beltraliny.testeuex.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Optional<User> findById(String id) {
        return this.userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public void update(String id, UserDTO userDTO) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) return;

        User userToBeUpdated = user.get();
        if (!userDTO.name().isEmpty()) userToBeUpdated.setName(userDTO.name());
        if (!userDTO.username().isEmpty()) userToBeUpdated.setUsername(userDTO.username());
        if (!userDTO.password().isEmpty()) userToBeUpdated.setPassword(userDTO.password());

        this.userRepository.save(userToBeUpdated);
    }

    public void delete(String id) {
        boolean userExists = this.userRepository.existsById(id);
        if (!userExists) return;

        this.userRepository.deleteById(id);
    }
}
