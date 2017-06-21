package com.doit.wheels.dao.repositories;

import com.doit.wheels.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
}