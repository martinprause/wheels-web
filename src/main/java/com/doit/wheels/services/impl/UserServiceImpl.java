package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.User;
import com.doit.wheels.dao.repositories.UserRepository;
import com.doit.wheels.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public User getUser(long id){
        return userRepository.findOne(id);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public User findUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

}
