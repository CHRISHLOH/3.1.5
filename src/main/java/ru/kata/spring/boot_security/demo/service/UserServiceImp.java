package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public UserServiceImp(UserRepository userRepository, @Lazy PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
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

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByID(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void saveUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email already exists");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

    }

    @Override
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

    @Override
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
            user.getRoles().clear();
            user.setRoles(updatedUser.getRoles());
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setAge(updatedUser.getAge());
            user.setEmail(updatedUser.getEmail());
            userRepository.save(user);
        }
    }
}


