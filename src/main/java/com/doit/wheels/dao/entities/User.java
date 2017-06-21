package com.doit.wheels.dao.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String username;

    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> role;

    public User() {

    }

    public User(String login, String password) {
        this.username = login;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRole() {
        return role;
    }

    public void setRole(Set<UserRole> role) {
        this.role = role;
    }
}
