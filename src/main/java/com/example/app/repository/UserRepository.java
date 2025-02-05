package com.example.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.app.model.User;

/**
 * Repository interface for the User entity.
 * This interface allows performing CRUD operations and custom queries for the Client entity.
 * It extends JpaRepository, which provides a set of default methods for interacting with the database.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
}
