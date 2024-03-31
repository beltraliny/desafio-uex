package com.github.beltraliny.testeuex.controllers;

import com.github.beltraliny.testeuex.models.Contact;
import com.github.beltraliny.testeuex.models.dtos.ContactDTO;
import com.github.beltraliny.testeuex.services.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contacts")
    public ResponseEntity<Contact> create(@RequestHeader("Authorization") String token, @RequestBody ContactDTO contactDTO) {
        Contact newContact = this.contactService.create(token, contactDTO);
        return ResponseEntity.ok(newContact);
    }

    @GetMapping("/{userId}/contacts/{id}")
    public ResponseEntity<Contact> findById(@PathVariable String userId, @PathVariable String id) {
        Contact contact = this.contactService.findById(userId, id);
        return ResponseEntity.ok(contact);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<Contact>> list(@RequestHeader("Authorization") String token) {
        List<Contact> contactList = this.contactService.list(token);
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
