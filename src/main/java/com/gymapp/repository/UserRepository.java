package com.gymapp.repository;

import com.gymapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    // This is a custom query Spring Data JPA will create for you.
    // It's essential for the login process to find a user by their email.
    Optional<Users> findByEmail(String email);

}