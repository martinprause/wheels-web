package com.doit.wheels.services;

import com.doit.wheels.dao.entities.Description;

import java.util.List;

public interface GenericService<T extends Description> {
    T save(T t);

    T update(T t);

    void delete(T t);

    List<T> findAll();
}
