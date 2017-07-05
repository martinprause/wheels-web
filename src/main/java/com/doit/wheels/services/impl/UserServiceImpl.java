package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.User;
import com.doit.wheels.dao.repositories.UserRepository;
import com.doit.wheels.services.UserService;
import com.doit.wheels.utils.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUser(long id) {
        return userRepository.findOne(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public User saveUser(User user) throws UserException {

        if (findUserByUsername(user.getUsername()) != null){
            throw new UserException();
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user){
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
