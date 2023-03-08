package com.example.webapp.controller;

import com.example.webapp.entity.User;
import com.example.webapp.exeption.UserAlreadyExistsException;
import com.example.webapp.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping
@Controller
public class UserController {
    private UserService userService;
    private static Logger logger = LogManager.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String getIndexPage(Model model) {
        logger.log(Level.INFO, "GET INDEX PAGE");
        return "index";
    }

    @GetMapping("/signup")
    public String getSignupPage(Model model) {
        logger.log(Level.INFO, "GET SIGNUP PAGE");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String postUserEntity(@ModelAttribute("user") User user, Model model) {
        Optional<User> candidate = null;
        try {
            candidate = Optional.ofNullable(userService.registerUser(user));
        } catch (UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/profile/" + candidate.get().getId();
    }

    @GetMapping("/signin")
    public String getSigninPage(Model model) {
        logger.log(Level.INFO, "GET SIGNUP PAGE");
        model.addAttribute("user", new User());
        return "signin";
    }

    @PostMapping("/signin")
    public String loginWithData(@ModelAttribute("user") User user, Model model) {
        Optional<User> candidate = userService.logIn(user.getEmail(), user.getPassword());
        if (candidate.isEmpty()) {
            return "redirect:/signin";
        } else {
            return "redirect:/profile/" + candidate.get().getId();
        }
    }

    @GetMapping("/profiles")
    public String getProfiles(Model model) {

        model.addAttribute("users", userService.getUsers());
        return "profiles";
    }

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable int id, Model model) {
        Optional<User> user = userService.getUserById(id);
        model.addAttribute("user", user.get());
        return "profile";
    }
}
