package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.AccessLevel;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.dao.repositories.UserRepository;
import com.doit.wheels.services.AccessLevelService;
import com.doit.wheels.services.UserService;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;
import com.doit.wheels.utils.enums.UserRoleEnum;
import com.doit.wheels.utils.exceptions.NoPermissionsException;
import com.doit.wheels.utils.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

    private final AccessLevelService accessLevelService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(GenericRepository<User> genericRepository,
                           UserRepository userRepository,
                           AccessLevelService accessLevelService,
                           PasswordEncoder passwordEncoder) {
        super(genericRepository);
        this.userRepository = userRepository;
        this.accessLevelService = accessLevelService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public User addNewUser(User user) throws UserException {
        if (findUserByUsername(user.getUsername()) != null) {
            throw new UserException();
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return super.save(user);
    }

    @Override
    public User updateUser(User user){
        if (user.getPassword() == null || user.getPassword().equals("")) {
            user.setPassword(findById(user.getId()).getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public void removeUserWithAccesses(User user) throws NoPermissionsException {
        User currentUser = findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        boolean access = currentUser.getAccesses().stream().anyMatch(dto -> dto.getAccessLevel() == AccessLevelTypeEnum.DeleteUser);
        if (access){
            if (user.getAccesses() != null && user.getAccesses().size() != 0) {
                for (AccessLevel accessLevel : user.getAccesses()) {
                    accessLevel.getUsers().remove(user);
                    accessLevelService.save(accessLevel);
                }
                user.getAccesses().clear();
            }
            delete(user);
        } else throw new NoPermissionsException("Permission for user + " + currentUser.getUsername() + " denied!");
    }

    @Override
    public List<User> findAllByRole(UserRoleEnum role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public boolean isCurrentUserAdmin() {
        return findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getRole() == UserRoleEnum.ADMIN;
    }

    @Override
    public boolean checkIfCurrentUserHasPermissions(AccessLevelTypeEnum AccessLevelTypeEnum) {
        User currentUser = findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return currentUser.getAccesses().stream().anyMatch(dto -> dto.getAccessLevel() == AccessLevelTypeEnum);
    }

}
