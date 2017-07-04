package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.Manufacturer;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.ManufacturerService;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerServiceImpl extends GenericServiceImpl<Manufacturer> implements ManufacturerService{

    public ManufacturerServiceImpl(GenericRepository<Manufacturer> genericRepository) {
        super(genericRepository);
    }
}
