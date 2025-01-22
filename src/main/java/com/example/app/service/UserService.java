package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.controller.user.UserDto;
import com.example.app.model.User;
import com.example.app.repository.UserRepository;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to the User entity.
 * It interacts with the UserRepository to perform CRUD operations.
 * Provides methods to get, create, update, and delete users.
 * This class is annotated with @Service to indicate that it is a Spring service component.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(UserDto userDto )
    {
        User createdUser = userRepository.save(new User(userDto));
        return createdUser;
    }

    public User updateUser(Long id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword()); 
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail( String email ){
        System.out.println( "Email: " + email );
        Optional<User> optionalUser = userRepository.findByEmail( email );
        if ( optionalUser.isPresent() )
        {
            return optionalUser.get();
        }
        else
        {
            return  null;
        }
    }
}
