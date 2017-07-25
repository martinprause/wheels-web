package com.doit.wheels.dao.repositories;

import com.doit.wheels.dao.entities.basic.AbstractModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericRepository<T extends AbstractModel> extends JpaRepository<T, Long> {}
