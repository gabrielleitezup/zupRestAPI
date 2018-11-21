package com.zup.br.CustomerAPI.repository;

import com.zup.br.CustomerAPI.model.City;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {

    Page<City> findByNameContaining(Pageable pageable, String name);
}
