package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.Guideline;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.GuidelineService;
import org.springframework.stereotype.Service;

@Service
public class GuidelineServiceImpl extends GenericServiceImpl<Guideline> implements GuidelineService {

    public GuidelineServiceImpl(GenericRepository<Guideline> genericRepository) {
        super(genericRepository);
    }
}
