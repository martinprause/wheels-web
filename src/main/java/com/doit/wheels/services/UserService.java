package com.doit.wheels.services;


import com.doit.wheels.dao.entities.User;
import com.doit.wheels.utils.exceptions.NoPermissionsException;
import com.doit.wheels.utils.exceptions.UserException;

import java.util.List;

public interface UserService extends GenericService<User>{

    User getUser(long id);

    User addNewUser(User user) throws UserException;

    User updateUser(User user);

    List<User> findAll();

    User findUserByUsername(String username);

    void removeUserWithAccesses(User user) throws NoPermissionsException;
}
