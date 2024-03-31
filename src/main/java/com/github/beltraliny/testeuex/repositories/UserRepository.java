package com.github.beltraliny.testeuex.repositories;

import com.github.beltraliny.testeuex.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, String id);
}
