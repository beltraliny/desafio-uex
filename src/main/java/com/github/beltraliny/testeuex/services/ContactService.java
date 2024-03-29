package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.Contact;
import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.ContactDTO;
import com.github.beltraliny.testeuex.repositories.ContactRepository;
import com.github.beltraliny.testeuex.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public UUID create(String userId, ContactDTO contactDTO) {
        User user = this.userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Contact newContact = new Contact(contactDTO);
        newContact.setUser(user);

        return this.contactRepository.save(newContact).getId();
    }

    public Contact findById(String userId, String id) {
        User user = this.userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return this.contactRepository.findByUserAndId(user, UUID.fromString(id));
    }

    public List<ContactDTO> list(String userId) {
        User user = this.userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<ContactDTO> contactDTOList = user.getContactList()
                .stream()
                .map(contact -> new ContactDTO(contact.getName(), contact.getCpf()))
                .toList();
        return contactDTOList;
    }
}
