package com.example.superdataworker.repository;

import com.example.superdataworker.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findByFirstNameStartingWithOrLastNameStartingWith(String name, String name1);

    Customer findByCustomerId(String customerId);
}