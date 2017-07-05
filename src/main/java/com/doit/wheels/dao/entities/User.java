package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.Contact;
import com.doit.wheels.utils.UserRoleEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="users")
public class User extends Contact {

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private String employeeNo;

    private String comment;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<AccessLevel> accesses;

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

    public Set<AccessLevel> getAccesses() {
        return accesses;
    }

    public void setAccesses(Set<AccessLevel> accesses) {
        this.accesses = accesses;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof User){
            if (((User) obj).getId().equals(this.getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = this.getId();
        result = 31 * result + getUsername().hashCode();
        return result;
    }
}
