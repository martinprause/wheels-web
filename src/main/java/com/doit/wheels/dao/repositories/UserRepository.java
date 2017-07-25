package com.doit.wheels.dao.repositories;

import com.doit.wheels.dao.entities.User;
import com.doit.wheels.utils.enums.UserRoleEnum;

import java.util.List;

public interface UserRepository extends GenericRepository<User> {

    User findUserByUsername(String username);

    List<User> findAllByRole(UserRoleEnum role);
}