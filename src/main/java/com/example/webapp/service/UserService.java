package com.example.webapp.service;

import com.example.webapp.entity.User;
import com.example.webapp.exeption.UserAlreadyExistsException;
import com.example.webapp.exeption.UserNotFoundException;
import com.example.webapp.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Primary
@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findUserById(id);
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<User>(userRepository.findAllUsers());
    }

    public Optional<User> logIn(String email, String password) {
        Optional<User> user = null;
        try {
            user = this.userRepository.authenticate(email, password);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User registerUser(User user) throws UserAlreadyExistsException {
        Optional<User> candidate = userRepository.findUserByEmail(user.getEmail());
        if (!candidate.isEmpty()) {
            throw new UserAlreadyExistsException();
        }
        user = userRepository.saveUser(user);
        return user;
    }
}
