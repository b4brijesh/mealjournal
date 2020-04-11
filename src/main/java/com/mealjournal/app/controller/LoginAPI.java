package com.mealjournal.app.controller;

import com.mealjournal.app.model.User;
import com.mealjournal.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class LoginAPI {

    /*@Autowired
    private ClientRepository clientRepository;*/

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/signup")
    public String registration() {
        return "registration.html";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity registration(@RequestParam("fullname") String name,
                                @RequestParam("username") String email,
                                @RequestParam("password") String password) {
        Optional<User> matchedUser = userRepository.findByEmail(email);
        if (matchedUser.isPresent()) return new ResponseEntity(HttpStatus.BAD_REQUEST); // todo: add message

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setSaltedHashedPass(password);
        userRepository.save(newUser);

        return new ResponseEntity(HttpStatus.OK);
    }
}
