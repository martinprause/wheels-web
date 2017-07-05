package com.doit.wheels.dao.entities;

import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.utils.AccessLevelType;

import javax.persistence.*;
import java.util.Set;

@Entity
public class AccessLevel extends AbstractModel {

    @Enumerated(value = EnumType.STRING)
    private AccessLevelType accessLevel;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_accesses",
            joinColumns = {@JoinColumn(name = "access_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> users;

    public AccessLevel() {
    }

    public AccessLevelType getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevelType accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
