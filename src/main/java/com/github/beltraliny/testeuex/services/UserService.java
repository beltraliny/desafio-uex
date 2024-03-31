package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.UserRequestDTO;
import com.github.beltraliny.testeuex.models.dtos.UserResponseDTO;
import com.github.beltraliny.testeuex.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final ContactService contactService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ContactService contactService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.contactService = contactService;
    }

    public User create(UserRequestDTO userRequestDTO) {
        Optional<User> user = this.findByUsername(userRequestDTO.username());
        if (user.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        User newUser = this.parseUser(userRequestDTO);
        boolean isValid = this.validateBeforeSave(newUser);
        if (!isValid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

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

    public void update(String id, UserRequestDTO userRequestDTO) {
        User userToBeUpdated = this.userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (userRequestDTO.name() != null) userToBeUpdated.setName(userRequestDTO.name());
        if (userRequestDTO.username() != null) userToBeUpdated.setUsername(userRequestDTO.username());
        if (userRequestDTO.password() != null) userToBeUpdated.setPassword(userRequestDTO.password());

        this.userRepository.save(userToBeUpdated);
    }

    public void delete(UserRequestDTO userRequestDTO, String id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        boolean isPasswordMatch = this.passwordEncoder.matches(userRequestDTO.password(), user.getPassword());
        if (!isPasswordMatch) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        this.contactService.deleteByUser(user);
        this.userRepository.deleteById(id);
    }

    private User parseUser(UserRequestDTO userRequestDTO) {
        User user = new User(userRequestDTO);
        if (userRequestDTO.password() != null){
            user.setPassword(this.passwordEncoder.encode(userRequestDTO.password()));
        }
        return user;
    }

    private boolean validateBeforeSave(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) return false;
        if (user.getPassword() == null || user.getPassword().isEmpty()) return false;
        return true;
    }
}
