package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "role")
    private String role;

    public Role(String role) {
        this.role = role;
    }

    public Role() {}

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return getRole();
    }
}
