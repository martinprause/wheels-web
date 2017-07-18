package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.PrintJob;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.PrintJobService;
import org.springframework.stereotype.Service;

@Service
public class PrintJobServiceImpl extends GenericServiceImpl<PrintJob> implements PrintJobService {


    public PrintJobServiceImpl(GenericRepository<PrintJob> genericRepository) {
        super(genericRepository);
    }


}
