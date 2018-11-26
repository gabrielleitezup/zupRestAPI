package com.zup.br.customerapi.repository;

import com.zup.br.customerapi.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {

    Page<Customer> findByNameContaining(Pageable pageable, String name);
}
