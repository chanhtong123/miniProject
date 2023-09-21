package com.example.superdataworker.repository;

import com.example.superdataworker.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, String> {

    Apartment findByApartmentId(String ApartmentId);
}
