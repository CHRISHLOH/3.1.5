package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, @Lazy PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Hibernate.initialize(user.getRoles());

        return user;
    }

    @Transactional(readOnly = true)
    public User findByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByID(long id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email already exists");
        }

        Role userRole = roleRepository.findByRole(user.getRoles()
                .stream()
                .findFirst()
                .map(Role::getRole)
                .orElse(null));
        if (userRole == null) {
            throw new RuntimeException("Role not found");
        }

        user.getRoles().clear();
        user.getRoles().add(userRole);
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);


    }


    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getRoles().clear();
            userRepository.delete(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void updateUser(User updatedUser) {
        Optional<User> optionalUser = getUserByID(updatedUser.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getUsername().equals(updatedUser.getEmail()) && userRepository.findByEmail(updatedUser.getEmail()) != null) {
                throw new RuntimeException("Email already exists");
            }
            if (!user.getPassword().equals(updatedUser.getPassword())) {
                String encodedPassword = bCryptPasswordEncoder.encode(updatedUser.getPassword());
                user.setPassword(encodedPassword);
            }

            Role userRole = roleRepository.findByRole(updatedUser.getRoles()
                    .stream()
                    .findFirst()
                    .map(Role::getRole)
                    .orElse(null));
            if (userRole == null) {
                throw new RuntimeException("Role not found");
            }
            System.out.println(userRole);
            user.getRoles().clear();
            user.getRoles().add(userRole);
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setAge(updatedUser.getAge());
            user.setEmail(updatedUser.getEmail());
            userRepository.save(user);
        }
    }

}


