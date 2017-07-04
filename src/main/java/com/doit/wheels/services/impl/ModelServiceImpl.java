package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.Model;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.ModelService;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImpl extends GenericServiceImpl<Model> implements ModelService {

    public ModelServiceImpl(GenericRepository<Model> genericRepository) {
        super(genericRepository);
    }
}
