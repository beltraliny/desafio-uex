package com.github.beltraliny.testeuex.controllers;

import com.github.beltraliny.testeuex.models.user.User;
import com.github.beltraliny.testeuex.models.user.UserDTO;
import com.github.beltraliny.testeuex.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserDTO userDTO) {
        UUID userId = this.userService.create(userDTO);
        return ResponseEntity.created(URI.create("/api/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findById(@PathVariable("userId") String userId) {
        Optional<User> user = this.userService.findById(userId);

        if(!user.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user.get());
    }

    @GetMapping
    public ResponseEntity<List<User>> list() {
        List<User> userList = this.userService.list();
        return ResponseEntity.ok(userList);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(@PathVariable String userId, @RequestBody UserDTO userDTO) {
        this.userService.update(userId, userDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId) {
        this.userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
