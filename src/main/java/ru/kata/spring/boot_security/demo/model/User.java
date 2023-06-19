package ru.kata.spring.boot_security.demo.model;

import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotEmpty
    @Size(min = 2, max = 30, message = "firstName should be between 2 and 30 characters.")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 30, message = "lastName should be between 2 and 30 characters.")
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Range(min = 0, max = Byte.MAX_VALUE, message = "Invalid age range")
    @Column(name = "age")
    private byte age;

    @NotEmpty
    @Size(min = 2, max = 80, message = "password should be between 2 and 30 characters.")
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Size(min = 2, max = 30, message = "email should be between 2 and 30 characters.")
    @Column(name = "email")
    private String email;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set <Role> roles = new HashSet<>();

    public User(long id,  String firstName, String lastName, byte age, String password, String email, Set<Role> roles) {
        this.id = id;

        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public void setPassword(String password){
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }
}
