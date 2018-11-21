package com.zup.br.CustomerAPI.controllers;

import com.zup.br.CustomerAPI.model.City;
import com.zup.br.CustomerAPI.repository.CityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class CityController {

    @Autowired
    CityRepository cityRepository;

    @PostMapping("/cities")
    @ResponseStatus(HttpStatus.CREATED)
    public City create(@Valid @RequestBody City city) {
        return cityRepository.save(city);
    }

    @GetMapping("/cities")
    public Page<City> read(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @PutMapping("/cities/{id}")
    public ResponseEntity<City> update(@PathVariable int id, @Valid @RequestBody City city) {
        city.setId(id);
        if (cityRepository.findById(city.getId()).isPresent()) {
            return new ResponseEntity<>(cityRepository.save(city), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(city, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cities/{id}")
    public Optional<City> findById(@PathVariable int id) {
        return cityRepository.findById(id);
    }

    @GetMapping("/cities/search")
    public Page<City> findByName(
            Pageable pageable,
            @RequestParam(value = "name", required = true) String name) {

        return cityRepository.findByNameCity(pageable, name);
    }

    @DeleteMapping("/cities/{id}")
    public void delete(@PathVariable Integer id) {
        cityRepository.deleteById(id);
    }

}