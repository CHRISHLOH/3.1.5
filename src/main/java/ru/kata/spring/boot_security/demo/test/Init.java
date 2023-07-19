package ru.kata.spring.boot_security.demo.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;


import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public Init(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        Role userRole = new Role(1, "ROLE_USER");
        Role adminRole = new Role(2, "ROLE_ADMIN");

        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        Set<Role> roleUser = new HashSet<>();
        roleUser.add(userRole);

        Set<Role> roleAdmin = new HashSet<>();
        roleAdmin.add(adminRole);


        User user1 = new User(1L, "User", "User", (byte) 25, "100", "User@example.com", roleUser);
        User user2 = new User(2L, "Admin", "Admin", (byte) 30, "100", "Admin@example.com", roleAdmin);


        userService.saveUser(user1);
        userService.saveUser(user2);
    }
}