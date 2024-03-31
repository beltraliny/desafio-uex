package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.models.Contact;
import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.ContactDTO;
import com.github.beltraliny.testeuex.repositories.ContactRepository;
import com.github.beltraliny.testeuex.repositories.UserRepository;
import com.github.beltraliny.testeuex.security.TokenService;
import com.github.beltraliny.testeuex.utils.CpfUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository, TokenService tokenService) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public Contact create(String token, ContactDTO contactDTO) {
        String username = tokenService.validateTokenAndRetrieveUsername(token);
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Contact newContact = new Contact(contactDTO);
        newContact.setUser(user);

        boolean isValid = this.validateBeforeSave(newContact);
        if (!isValid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return this.contactRepository.save(newContact);
    }

    public Contact findById(String userId, String id) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return this.contactRepository.findByUserAndId(user, id);
    }

    public List<Contact> list(String token) {
        String username = tokenService.validateTokenAndRetrieveUsername(token);
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getContactList();
    }

    private boolean validateBeforeSave(Contact contact) {
        return CpfUtils.isValid(contact.getCpf());
    }
}
