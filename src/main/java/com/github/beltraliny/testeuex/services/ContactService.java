package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.integrations.GoogleMapsService;
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
import java.util.Map;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final GoogleMapsService googleMapsService;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository,
                          TokenService tokenService, GoogleMapsService googleMapsService) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.googleMapsService = googleMapsService;
    }

    public Contact create(String token, ContactDTO contactDTO) {
        String username = tokenService.validateTokenAndRetrieveUsername(token);
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Contact contact = this.parseContact(contactDTO, user);

        boolean isValid = this.validateBeforeSave(contact);
        if (!isValid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return this.contactRepository.save(contact);
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
        if (contact.getName() == null) return false;
        if (contact.getCpf() == null) return false;
        if (contact.getPhoneNumber() == null) return false;
        if (contact.getStreet() == null) return false;
        if (contact.getNumber() == null) return false;
        if (contact.getNeighborhood() == null) return false;
        if (contact.getCity() == null) return false;
        if (contact.getState() == null) return false;
        if (contact.getCountry() == null) return false;
        if (contact.getPostalCode() == null) return false;

        return CpfUtils.isValid(contact.getCpf());
    }

    private Contact parseContact(ContactDTO contactDTO, User user) {
        Contact parsedContact = new Contact(contactDTO);
        parsedContact.setUser(user);

        Map<String, String> coordinates = googleMapsService.findLatLogByAddress(parsedContact.getFullAddress());
        if (coordinates != null) {
            parsedContact.setLatitude(coordinates.get("latitude"));
            parsedContact.setLongitude(coordinates.get("longitude"));
        }

        return parsedContact;
    }
}
