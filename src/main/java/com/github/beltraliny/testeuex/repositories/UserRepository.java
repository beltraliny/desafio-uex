package com.github.beltraliny.testeuex.repositories;

import com.github.beltraliny.testeuex.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
