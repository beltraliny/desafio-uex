package com.github.beltraliny.testeuex.controllers;

import com.github.beltraliny.testeuex.models.Contact;
import com.github.beltraliny.testeuex.models.dtos.ContactDTO;
import com.github.beltraliny.testeuex.services.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/{userId}/contacts")
    public ResponseEntity<Contact> create(@PathVariable String userId, @RequestBody ContactDTO contactDTO) {
        UUID id = this.contactService.create(userId, contactDTO);
        return ResponseEntity.created(URI.create("/api/users/" + userId + "/contact/" + id.toString())).build();
    }

    @GetMapping("/{userId}/contacts/{id}")
    public ResponseEntity<Contact> findById(@PathVariable String userId, @PathVariable String id) {
        Contact contact = this.contactService.findById(userId, id);
        return ResponseEntity.ok(contact);
    }

    @GetMapping("/{userId}/contacts")
    public ResponseEntity<List<ContactDTO>> list(@PathVariable String userId) {
        List<ContactDTO> contactList = this.contactService.list(userId);
        return ResponseEntity.ok(contactList);
    }

    @PutMapping("/{userId}/contacts/{id}")
    public ResponseEntity<Contact> update(@PathVariable String userId, @PathVariable String id, @RequestBody ContactDTO contactDTO) {
        return null;
    }

    @DeleteMapping("/{userId}/contacts/{id}")
    public ResponseEntity<Contact> delete(@PathVariable String userId, @PathVariable String id) {
        return null;
    }
}
