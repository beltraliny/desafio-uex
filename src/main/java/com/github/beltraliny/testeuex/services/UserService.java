package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.UserDTO;
import com.github.beltraliny.testeuex.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String create(UserDTO userDTO) {
        User newUser = new User(userDTO);
        User savedUser = this.userRepository.save(newUser);
        return savedUser.getId();
    }

    public Optional<User> findById(String id) {
        return this.userRepository.findById(id);
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
