package com.doit.wheels.dao.repositories;

import com.doit.wheels.dao.entities.Description;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericRepository<T extends Description> extends JpaRepository<T, Integer> {}
