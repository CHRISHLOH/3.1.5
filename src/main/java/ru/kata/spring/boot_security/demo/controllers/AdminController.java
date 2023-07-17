package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> adminPage() {
       return userService.getUserList();
    }


    @GetMapping("/{id}")
    public User showUserDetails(@PathVariable long id) {
        Optional<User> optionalUser = userService.getUserByID(id);
        if (optionalUser.isPresent()) {
           return optionalUser.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @GetMapping("/user")
    public User showUserDetails( Principal principal) {
        return userService.findByUsername(principal.getName());
    }
    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }



    @PatchMapping
    public ResponseEntity<HttpStatus> update(@RequestBody User user) {

            userService.updateUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}