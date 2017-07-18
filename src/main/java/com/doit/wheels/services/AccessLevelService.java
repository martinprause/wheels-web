package com.doit.wheels.services;


import com.doit.wheels.dao.entities.AccessLevel;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;

public interface AccessLevelService {

    AccessLevel save(AccessLevel accessLevel);

    AccessLevel findAccessLevelByAccessLevel(AccessLevelTypeEnum AccessLevelTypeEnum);


}
