package com.github.beltraliny.testeuex.controllers;

import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.UserDTO;
import com.github.beltraliny.testeuex.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserDTO userDTO) {
        String newUserId = this.userService.create(userDTO);
        return ResponseEntity.created(URI.create("/api/users/" + newUserId)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        Optional<User> user = this.userService.findById(id);

        if(!user.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user.get());
    }

    @GetMapping
    public ResponseEntity<List<User>> list() {
        List<User> userList = this.userService.list();
        return ResponseEntity.ok(userList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody UserDTO userDTO) {
        this.userService.update(id, userDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
