package com.doit.wheels.dao.repositories;

import com.doit.wheels.dao.entities.User;
import com.doit.wheels.utils.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    List<User> findAllByRole(UserRoleEnum role);
}