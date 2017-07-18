package com.doit.wheels.dao.repositories;

import com.doit.wheels.dao.entities.PrintJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintJobRepository extends JpaRepository<PrintJob, Long> {

}