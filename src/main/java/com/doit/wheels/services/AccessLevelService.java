package com.doit.wheels.services;


import com.doit.wheels.dao.entities.AccessLevel;
import com.doit.wheels.utils.AccessLevelType;

public interface AccessLevelService {

    AccessLevel save(AccessLevel accessLevel);

    AccessLevel findAccessLevelByAccessLevel(AccessLevelType accessLevelType);


}
