package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.basic.AbstractModel;
import com.doit.wheels.dao.repositories.GenericRepository;
import com.doit.wheels.services.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class GenericServiceImpl<T extends AbstractModel> implements GenericService<T> {
    private final GenericRepository<T> genericRepository;

    @Autowired
    public GenericServiceImpl(GenericRepository<T> genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Override
    @Transactional
    public T save(T t) {
        return genericRepository.save(t);
    }

    @Override
    public T update(T t) {
        return genericRepository.save(t);
    }

    @Override
    public void delete(T t) {
        genericRepository.delete(t);
    }

    @Override
    public T findById(Long id) {
        return genericRepository.findOne(id);
    }

    @Override
    @Transactional
    public List<T> findAll() {
        return genericRepository.findAll();
    }
}
