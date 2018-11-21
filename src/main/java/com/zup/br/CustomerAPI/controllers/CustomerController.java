package com.zup.br.CustomerAPI.controllers;

import com.zup.br.CustomerAPI.model.Customer;
import com.zup.br.CustomerAPI.pagination.JSONPage;
import com.zup.br.CustomerAPI.pagination.PageImplementation;
import com.zup.br.CustomerAPI.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping("/customers")
    public JSONPage read(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);
        return PageImplementation.loadPage(customers, "customers");
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> update(@PathVariable int id, @Valid @RequestBody Customer customer) {
        customer.setId(id);
        if (customerRepository.findById(customer.getId()).isPresent()) {
            return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(customer, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/customers/{id}")
    public Optional<Customer> findById(@PathVariable int id) {
        return customerRepository.findById(id);
    }

    @GetMapping("/customers/search")
    public JSONPage findByName(
            Pageable pageable,
            @RequestParam(value = "name", required = true) String name) {
        Page<Customer> customers = customerRepository.findByNameCustomer(pageable, name);
        return PageImplementation.loadPage(customers, "customers");
    }

    @DeleteMapping("/customers/{id}")
    public void delete(@PathVariable int id) {
        customerRepository.deleteById(id);
    }

}
