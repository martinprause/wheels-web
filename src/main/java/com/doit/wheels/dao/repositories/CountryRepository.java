package com.doit.wheels.dao.repositories;


import com.doit.wheels.dao.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long>{
}
