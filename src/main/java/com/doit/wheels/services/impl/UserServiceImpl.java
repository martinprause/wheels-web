package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.User;
import com.doit.wheels.dao.entities.UserRole;
import com.doit.wheels.dao.repositories.UserRepository;
import com.doit.wheels.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserServiceImpl implements UserService, UserDetailsService {

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
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(s);
        if (user == null)
            throw new UsernameNotFoundException("User not found");

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (UserRole userRole : user.getRole()) {
            authorities.add(new SimpleGrantedAuthority(userRole.getRole()));
        }

        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), authorities);
    }
}
