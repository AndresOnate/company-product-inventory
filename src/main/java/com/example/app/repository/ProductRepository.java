package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.app.model.Product;

/**
 * Repository interface for the Product entity.
 * This interface allows performing CRUD operations and custom queries for the Product entity.
 * It extends JpaRepository, which provides a set of default methods for interacting with the database.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
