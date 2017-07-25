package com.doit.wheels.services;


import com.doit.wheels.dao.entities.User;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;
import com.doit.wheels.utils.enums.UserRoleEnum;
import com.doit.wheels.utils.exceptions.NoPermissionsException;
import com.doit.wheels.utils.exceptions.UserException;

import java.util.List;

public interface UserService extends GenericService<User>{

    User addNewUser(User user) throws UserException;

    User updateUser(User user);

    User findUserByUsername(String username);

    void removeUserWithAccesses(User user) throws NoPermissionsException;

    List<User> findAllByRole(UserRoleEnum role);

    boolean checkIfCurrentUserHasPermissions(AccessLevelTypeEnum accessLevelTypeEnum);

    boolean isCurrentUserAdmin();
}
