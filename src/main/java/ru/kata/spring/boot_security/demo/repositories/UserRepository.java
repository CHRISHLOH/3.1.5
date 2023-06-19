package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail (String username);
}
