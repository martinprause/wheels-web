package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.Country;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.CountryService;
import org.springframework.stereotype.Service;

@Service
public class CountryServiceImpl extends GenericServiceImpl<Country> implements CountryService {

    public CountryServiceImpl(GenericRepository<Country> genericRepository) {
        super(genericRepository);
    }
}
