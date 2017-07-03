package com.doit.wheels.services;

import com.doit.wheels.dao.entities.Country;

import java.util.List;

public interface CountryService {
    Country saveCountry(Country country);

    List<Country> findAll();
}
