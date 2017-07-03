package com.doit.wheels.dao.entities;

import com.doit.wheels.utils.UserRoleEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User extends Contact{

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private String employeeNo;

    private String comment;

//    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
//    private Set<AccessLevel> accessLevels = new HashSet<>();

    public User() {

    }

    public User(String login, String password) {
        this.username = login;
        this.password = password;
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

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

//    public Set<AccessLevel> getAccessLevels() {
//        return accessLevels;
//    }
//
//    public void setAccessLevels(Set<AccessLevel> accessLevels) {
//        this.accessLevels = accessLevels;
//    }
//
//    public void addAccessLevel(AccessLevel accessLevel) {
//        accessLevels.add(accessLevel);
//        accessLevel.getUsers().add(this);
//    }
}
