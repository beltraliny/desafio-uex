package com.github.beltraliny.testeuex.repositories;

import com.github.beltraliny.testeuex.models.Contact;
import com.github.beltraliny.testeuex.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    Contact findByUserAndId(User user, String id);

    boolean existsByUserAndCpf(User user, String cpf);
}
