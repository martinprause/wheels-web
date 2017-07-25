package com.doit.wheels.services;

import com.doit.wheels.dao.entities.basic.AbstractModel;

import java.util.List;

public interface GenericService<T extends AbstractModel> {

    T save(T t);

    T update(T t);

    void delete(T t);

    T findById(Long id);

    List<T> findAll();
}
