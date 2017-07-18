package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.AccessLevel;
import com.doit.wheels.dao.repositories.AccessLevelRepository;
import com.doit.wheels.services.AccessLevelService;
import com.doit.wheels.utils.enums.AccessLevelTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessLevelServiceImpl implements AccessLevelService {

    @Autowired
    AccessLevelRepository accessLevelRepository;

    @Override
    public AccessLevel save(AccessLevel accessLevel) {
        return accessLevelRepository.save(accessLevel);
    }

    @Override
    public AccessLevel findAccessLevelByAccessLevel(AccessLevelTypeEnum AccessLevelTypeEnum) {
        return accessLevelRepository.findAccessLevelByAccessLevel(AccessLevelTypeEnum);
    }
}
