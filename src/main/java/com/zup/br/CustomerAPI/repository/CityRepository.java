package com.zup.br.CustomerAPI.repository;

import com.zup.br.CustomerAPI.model.City;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CityRepository extends PagingAndSortingRepository<City, Integer> {

    Page<City> findByNameCity(Pageable pageable, String name);
}
