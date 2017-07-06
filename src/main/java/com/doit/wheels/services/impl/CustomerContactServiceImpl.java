package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.CustomerContact;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.CustomerContactService;
import org.springframework.stereotype.Service;

@Service
public class CustomerContactServiceImpl extends GenericServiceImpl<CustomerContact> implements CustomerContactService {
    public CustomerContactServiceImpl(GenericRepository<CustomerContact> genericRepository) {
        super(genericRepository);
    }
}
