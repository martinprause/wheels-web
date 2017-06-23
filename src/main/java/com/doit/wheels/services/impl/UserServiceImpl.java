package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.User;
import com.doit.wheels.dao.repositories.UserRepository;
import com.doit.wheels.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(long id) {
        return userRepository.findOne(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

}
