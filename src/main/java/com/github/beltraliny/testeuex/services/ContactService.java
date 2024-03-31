package com.github.beltraliny.testeuex.services;

import com.github.beltraliny.testeuex.integrations.GoogleMapsService;
import com.github.beltraliny.testeuex.models.Contact;
import com.github.beltraliny.testeuex.models.User;
import com.github.beltraliny.testeuex.models.dtos.ContactDTO;
import com.github.beltraliny.testeuex.repositories.ContactRepository;
import com.github.beltraliny.testeuex.repositories.UserRepository;
import com.github.beltraliny.testeuex.security.TokenService;
import com.github.beltraliny.testeuex.utils.CpfUtils;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ContactService {

    private final ContactRepository contactRepository;
    private final GoogleMapsService googleMapsService;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository,
                          TokenService tokenService, GoogleMapsService googleMapsService) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.googleMapsService = googleMapsService;
    }

    public Contact create(String token, ContactDTO contactDTO) {
        User user = this.retrieveUserFromToken(token);

        boolean contactAlreadyExists = this.contactRepository.existsByUserAndCpf(user, contactDTO.cpf());
        if (contactAlreadyExists) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Contact contact = this.parseContact(contactDTO, user);

        boolean isValid = this.validateBeforeSave(contact);
        if (!isValid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return this.contactRepository.save(contact);
    }

    public Contact findById(String token, String id) {
        User user = this.retrieveUserFromToken(token);
        return this.contactRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Contact> list(String token) {
        User user = this.retrieveUserFromToken(token);
        return user.getContactList();
    }

    public void update(String token, ContactDTO contactDTO, String id) {
        User user = this.retrieveUserFromToken(token);
        Contact contact = this.parseContactToBeUpdated(contactDTO, user, id);

        boolean isValid = this.validateBeforeSave(contact);
        if (!isValid) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        this.contactRepository.save(contact);
    }

    public void delete(String token, String id) {
        User user = this.retrieveUserFromToken(token);
        this.contactRepository.deleteByUserAndId(user, id);
    }

    public void deleteByUser(User user) {
        this.contactRepository.deleteByUser(user);
    }

    private boolean validateBeforeSave(Contact contact) {
        if (contact.getName() == null || contact.getName().isEmpty()) return false;
        if (contact.getCpf() == null || contact.getCpf().isEmpty()) return false;
        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().isEmpty()) return false;
        if (contact.getStreet() == null || contact.getStreet().isEmpty()) return false;
        if (contact.getNumber() == null || contact.getNumber().isEmpty()) return false;
        if (contact.getNeighborhood() == null || contact.getNeighborhood().isEmpty()) return false;
        if (contact.getCity() == null || contact.getCity().isEmpty()) return false;
        if (contact.getState() == null || contact.getState().isEmpty()) return false;
        if (contact.getCountry() == null || contact.getCountry().isEmpty()) return false;
        if (contact.getPostalCode() == null || contact.getPostalCode().isEmpty()) return false;

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

    private Contact parseContactToBeUpdated(ContactDTO contactDTO, User user, String id) {
        Contact contact = this.contactRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (contactDTO.name() != null)  contact.setName(contactDTO.name());
        if (contactDTO.phoneNumber() != null)  contact.setPhoneNumber(contactDTO.phoneNumber());
        if (contactDTO.street() != null)  contact.setStreet(contactDTO.street());
        if (contactDTO.number() != null)  contact.setNumber(contactDTO.number());
        if (contactDTO.neighborhood() != null)  contact.setNeighborhood(contactDTO.neighborhood());
        if (contactDTO.city() != null)  contact.setCity(contactDTO.city());
        if (contactDTO.state() != null)  contact.setState(contactDTO.state());
        if (contactDTO.country() != null)  contact.setCountry(contactDTO.country());
        if (contactDTO.complement() != null)  contact.setComplement(contactDTO.complement());
        if (contactDTO.postalCode() != null)  contact.setPostalCode(contactDTO.postalCode());

        return contact;
    }

    private User retrieveUserFromToken(String token) {
        String username = tokenService.validateTokenAndRetrieveUsername(token);
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
