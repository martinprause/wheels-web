package com.doit.wheels.services;


import com.doit.wheels.dao.entities.User;
import com.doit.wheels.utils.exceptions.UserException;

import java.util.List;

public interface UserService {

    User getUser(long id);

    User saveUser(User user) throws UserException;

    User updateUser(User user);

    List<User> findAll();

    User findUserByUsername(String username);

}
