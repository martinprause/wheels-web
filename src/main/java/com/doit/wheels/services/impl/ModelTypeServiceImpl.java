package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.ModelType;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.ModelTypeService;
import org.springframework.stereotype.Service;

@Service
public class ModelTypeServiceImpl extends GenericServiceImpl<ModelType> implements ModelTypeService {
    public ModelTypeServiceImpl(GenericRepository<ModelType> genericRepository) {
        super(genericRepository);
    }
}
