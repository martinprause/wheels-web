package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.ValveType;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.ValveTypeService;
import org.springframework.stereotype.Service;

@Service
public class ValveTypeServiceImpl extends GenericServiceImpl<ValveType> implements ValveTypeService {

    public ValveTypeServiceImpl(GenericRepository<ValveType> genericRepository) {
        super(genericRepository);
    }
}
