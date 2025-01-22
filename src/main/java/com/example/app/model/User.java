package com.example.app.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.example.app.controller.user.UserDto;
import com.example.app.enums.RoleEnum;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user within the system.
 * This class is mapped as an entity in a relational database using JPA annotations.
 * It contains details about the user, such as their email, password, and role (admin or external).
 * Lombok annotations are used to simplify the creation of constructors, getters, setters, 
 * and builder pattern.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    List<RoleEnum> roles;

    public User( UserDto userDto )
    {
        name = userDto.getName();
        email = userDto.getEmail();
        System.out.println(email);
        password = BCrypt.hashpw( userDto.getPassword(), BCrypt.gensalt() );
        roles = new ArrayList<>();
        roles.add(RoleEnum.ADMIN); 
        
    }
}