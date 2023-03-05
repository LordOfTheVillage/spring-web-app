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
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    public Optional<User> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<User>(userRepository.getUsers());
    }

    public Optional<User> logIn(String email, String password) {
        Optional<User> user;
        try {
            user = Optional.ofNullable(this.getUserByEmail(email));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User registerUser(User user) throws UserAlreadyExistsException {
        Optional<User> candidate = null;
        try {
            candidate = Optional.ofNullable(getUserByEmail(user.getEmail()));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (candidate.isPresent()) {
            throw new UserAlreadyExistsException();
        }
//        UserJPARepository repo = null;
//        repo.save(user);

        return user;
    }
}
