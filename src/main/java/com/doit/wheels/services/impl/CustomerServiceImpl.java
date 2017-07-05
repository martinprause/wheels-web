package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.Customer;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends GenericServiceImpl<Customer> implements CustomerService {
    public CustomerServiceImpl(GenericRepository<Customer> genericRepository) {
        super(genericRepository);
    }
}
