package ru.kata.spring.boot_security.demo.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
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
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }


    public Optional<User> getUserByID(long id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }

        Role userRole = roleRepository.findByRole("ROLE_USER");
        if (userRole == null) {
            throw new RuntimeException("Role not found");
        }

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

    public void updateUser(long id, User updatedUser) {
        Optional<User> optionalUser = getUserByID(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getUsername().equals(updatedUser.getUsername()) && userRepository.findByUsername(updatedUser.getUsername()) != null) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(updatedUser.getUsername());
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setEmail(updatedUser.getEmail());
            userRepository.save(user);
        }
    }
}


