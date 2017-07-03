package com.doit.wheels.services.impl;

import com.doit.wheels.dao.entities.Country;
import com.doit.wheels.dao.repositories.CountryRepository;
import com.doit.wheels.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService{

    @Autowired
    private
    CountryRepository countryRepository;

    @Override
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}
